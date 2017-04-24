package com.wshsoft.mybatis.plugins;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.binding.MapperMethod.ParamMap;
import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.UnknownTypeHandler;

import com.wshsoft.mybatis.annotations.TableField;
import com.wshsoft.mybatis.annotations.Version;
import com.wshsoft.mybatis.mapper.EntityWrapper;
import com.wshsoft.mybatis.toolkit.PluginUtils;
import com.wshsoft.mybatis.toolkit.StringUtils;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.update.Update;

/**
 * <p>
 * MyBatis乐观锁插件
 * </p>
 * <p>
 * 
 * <pre>
 * 之前：update user set name = ?, password = ? where id = ?
 * 之后：update user set name = ?, password = ?, version = version+1 where id = ? and version = ?
 * 对象上的version字段上添加{@link Version}注解
 * sql可以不需要写version字段,只要对象version有值就会更新
 * 支持short,Short,int Integer, long Long, Date Timestamp
 * 其他类型可以自定义实现,注入versionHandlers,多个以逗号分隔
 * </pre>
 * 
 * @author Carry xie
 * @since 2017-04-08
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public final class OptimisticLockerInterceptor implements Interceptor {

	/**
	 * 根据对象类型缓存version基本信息
	 */
	private static final Map<Class<?>, LockerCache> versionCache = new ConcurrentHashMap<>();

	/**
	 * 根据version字段类型缓存的处理器
	 */
	private static final Map<Class<?>, VersionHandler<?>> typeHandlers = new HashMap<>();
	private static final Expression RIGHT_EXPRESSION = new Column("?");

	static {
		ShortTypeHandler shortTypeHandler = new ShortTypeHandler();
		typeHandlers.put(short.class, shortTypeHandler);
		typeHandlers.put(Short.class, shortTypeHandler);

		IntegerTypeHandler integerTypeHandler = new IntegerTypeHandler();
		typeHandlers.put(int.class, integerTypeHandler);
		typeHandlers.put(Integer.class, integerTypeHandler);

		LongTypeHandler longTypeHandler = new LongTypeHandler();
		typeHandlers.put(long.class, longTypeHandler);
		typeHandlers.put(Long.class, longTypeHandler);

		typeHandlers.put(Date.class, new DateTypeHandler());
		typeHandlers.put(Timestamp.class, new TimestampTypeHandler());
	}

	private volatile ParameterMapping parameterMapping;

	/**
	 * 注册处理器
	 */
	private static void registerHandler(VersionHandler<?> versionHandler) {
		Type[] genericInterfaces = versionHandler.getClass().getGenericInterfaces();
		ParameterizedType parameterizedType = (ParameterizedType) genericInterfaces[0];
		Class<?> type = (Class<?>) parameterizedType.getActualTypeArguments()[0];
		typeHandlers.put(type, versionHandler);
	}

	@Override
	public Object intercept(Invocation invocation) throws Exception {
		StatementHandler statementHandler = (StatementHandler) PluginUtils.realTarget(invocation.getTarget());
		MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
		// 先判断是不是真正的UPDATE操作
		MappedStatement ms = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
		if (!ms.getSqlCommandType().equals(SqlCommandType.UPDATE)) {
			return invocation.proceed();
		}
		BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
		// 获得参数类型,去缓存中快速判断是否有version注解才继续执行
		Class<?> parameterClass = ms.getParameterMap().getType();
		LockerCache lockerCache = versionCache.get(parameterClass);
		if (lockerCache != null) {
			if (lockerCache.lock) {
				processChangeSql(ms, boundSql, lockerCache);
			}
		} else {
			Field versionField = getVersionField(parameterClass);
			if (versionField != null) {
				if (!typeHandlers.containsKey(versionField.getType())) {
					throw new TypeException("乐观锁不支持" + versionField.getType().getName() + "类型,请自定义实现");
				}
				final TableField tableField = versionField.getAnnotation(TableField.class);
				String versionColumn = versionField.getName();
				if (tableField != null) {
					versionColumn = tableField.value();
				}
				LockerCache lc = new LockerCache(true, versionColumn, versionField);
				versionCache.put(parameterClass, lc);
				processChangeSql(ms, boundSql, lc);
			} else {
				versionCache.put(parameterClass, new LockerCache(false));
			}
		}
		return invocation.proceed();

	}

	private Field getVersionField(Class<?> parameterClass) {
		if (parameterClass != Object.class) {
			for (Field field : parameterClass.getDeclaredFields()) {
				if (field.isAnnotationPresent(Version.class)) {
					field.setAccessible(true);
					return field;
				}
			}
			return getVersionField(parameterClass.getSuperclass());
		}
		return null;

	}

	private void processChangeSql(MappedStatement ms, BoundSql boundSql, LockerCache lockerCache) throws Exception {
		Field versionField = lockerCache.field;
		String versionColumn = lockerCache.column;
		Object parameterObject = boundSql.getParameterObject();
		if (parameterObject instanceof ParamMap) {
			ParamMap<?> paramMap = (ParamMap<?>) parameterObject;
			parameterObject = paramMap.get("et");
			EntityWrapper<?> entityWrapper = (EntityWrapper<?>) paramMap.get("ew");
			if (entityWrapper != null) {
				Object entity = entityWrapper.getEntity();
				if (entity != null && versionField.get(entity) == null) {
					changSql(ms, boundSql, versionField, versionColumn, parameterObject);
				}
			}
		} else {
			changSql(ms, boundSql, versionField, versionColumn, parameterObject);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void changSql(MappedStatement ms, BoundSql boundSql, Field versionField, String versionColumn,
			Object parameterObject) throws Exception {
		final Object versionValue = versionField.get(parameterObject);
		if (versionValue != null) {// 先判断传参是否携带version,没带跳过插件
			Configuration configuration = ms.getConfiguration();
			// 给字段赋新值
			VersionHandler targetHandler = typeHandlers.get(versionField.getType());
			targetHandler.plusVersion(parameterObject, versionField, versionValue);
			// 处理where条件,添加?
			Update jsqlSql = (Update) CCJSqlParserUtil.parse(boundSql.getSql());
			BinaryExpression expression = (BinaryExpression) jsqlSql.getWhere();
			if (expression != null && !expression.toString().contains(versionColumn)) {
				EqualsTo equalsTo = new EqualsTo();
				equalsTo.setLeftExpression(new Column(versionColumn));
				equalsTo.setRightExpression(RIGHT_EXPRESSION);
				jsqlSql.setWhere(new AndExpression(equalsTo, expression));
				List<ParameterMapping> parameterMappings = new LinkedList<>(boundSql.getParameterMappings());
				parameterMappings.add(jsqlSql.getExpressions().size(), getVersionMappingInstance(configuration));
				MetaObject boundSqlMeta = configuration.newMetaObject(boundSql);
				boundSqlMeta.setValue("sql", jsqlSql.toString());
				boundSqlMeta.setValue("parameterMappings", parameterMappings);
			}
			// 设置参数
			boundSql.setAdditionalParameter("originVersionValue", versionValue);
		}
	}

    private ParameterMapping getVersionMappingInstance(Configuration configuration) {
        if (parameterMapping == null) {
            synchronized (OptimisticLockerInterceptor.class) {
                if (parameterMapping == null) {
                    parameterMapping = new ParameterMapping.Builder(configuration, "originVersionValue", new UnknownTypeHandler(configuration.getTypeHandlerRegistry())).build();
                }
            }
        }
        return parameterMapping;
    }

	@Override
	public Object plugin(Object target) {
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

	@Override
	public void setProperties(Properties properties) {
		String versionHandlers = properties.getProperty("versionHandlers");
		if (StringUtils.isNotEmpty(versionHandlers)) {
			for (String handlerClazz : versionHandlers.split(",")) {
				try {
					registerHandler((VersionHandler<?>) Class.forName(handlerClazz).newInstance());
				} catch (Exception e) {
					throw ExceptionFactory.wrapException("乐观锁插件自定义处理器注册失败", e);
				}
			}
		}
	}

	// *****************************基本类型处理器*****************************
	private static class ShortTypeHandler implements VersionHandler<Short> {

		@Override
		public void plusVersion(Object paramObj, Field field, Short versionValue) throws Exception {
			field.set(paramObj, (short) (versionValue + 1));
		}
	}

	private static class IntegerTypeHandler implements VersionHandler<Integer> {

		@Override
		public void plusVersion(Object paramObj, Field field, Integer versionValue) throws Exception {
			field.set(paramObj, versionValue + 1);
		}
	}

	private static class LongTypeHandler implements VersionHandler<Long> {

		@Override
		public void plusVersion(Object paramObj, Field field, Long versionValue) throws Exception {
			field.set(paramObj, versionValue + 1);
		}
	}

	// ***************************** 时间类型处理器*****************************
	private static class DateTypeHandler implements VersionHandler<Date> {

		@Override
		public void plusVersion(Object paramObj, Field field, Date versionValue) throws Exception {
			field.set(paramObj, new Date());
		}
	}

	private static class TimestampTypeHandler implements VersionHandler<Timestamp> {

		@Override
		public void plusVersion(Object paramObj, Field field, Timestamp versionValue) throws Exception {
			field.set(paramObj, new Timestamp(new Date().getTime()));
		}
	}

	/**
	 * 缓存对象
	 */
	private class LockerCache {
		private boolean lock;
		private String column;
		private Field field;

		LockerCache(Boolean lock) {
			this.lock = lock;
		}

		LockerCache(Boolean lock, String column, Field field) {
			this.lock = lock;
			this.column = column;
			this.field = field;
		}
	}

}