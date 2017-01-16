package com.wshsoft.mybatis.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;

import com.wshsoft.mybatis.exceptions.MybatisExtendsException;
import com.wshsoft.mybatis.toolkit.SqlUtils;
import com.wshsoft.mybatis.toolkit.StringUtils;
import com.wshsoft.mybatis.toolkit.SystemClock;

/**
 * <p>
 * 性能分析拦截器，用于输出每条 SQL 语句及其执行时间
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-07-07
 */
@Intercepts({
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class,
				ResultHandler.class }),
		@Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class PerformanceInterceptor implements Interceptor {

	/**
	 * SQL 执行最大时长，超过自动停止运行，有助于发现问题。
	 */
	private long maxTime = 0;

	private boolean format = false;

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		Object parameterObject = null;
		if (invocation.getArgs().length > 1) {
			parameterObject = invocation.getArgs()[1];
		}

		String statementId = mappedStatement.getId();
		BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
		Configuration configuration = mappedStatement.getConfiguration();
		String sql = SqlUtils.sqlFormat(boundSql.getSql(), format);

		List<String> params = getParams(boundSql, parameterObject, configuration);

		long start = SystemClock.now();
		Object result = invocation.proceed();
		long end = SystemClock.now();
		long timing = end - start;
		System.err.println(" Time：" + timing + " ms" + " - ID：" + statementId + "\n SQL Params:" + params.toString()
				+ "\n Execute SQL：" + sql + "\n");
		if (maxTime >0 && timing > maxTime) {
			throw new MybatisExtendsException(" The SQL execution time is too large, please optimize ! ");
		}
		return result;
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

	@Override
	public void setProperties(Properties prop) {
		// TODO
	}

	private List<String> getParams(BoundSql boundSql, Object parameterObject, Configuration configuration) {
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
		List<String> params = new ArrayList<String>();
		if (parameterMappings != null) {
			for (int i = 0; i < parameterMappings.size(); i++) {
				ParameterMapping parameterMapping = parameterMappings.get(i);
				if (parameterMapping.getMode() != ParameterMode.OUT) {
					Object value;
					String propertyName = parameterMapping.getProperty();
					if (boundSql.hasAdditionalParameter(propertyName)) {
						value = boundSql.getAdditionalParameter(propertyName);
					} else if (parameterObject == null) {
						value = null;
					} else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else {
						MetaObject metaObject = configuration.newMetaObject(parameterObject);
						value = metaObject.getValue(propertyName);
					}
					params.add(StringUtils.sqlParam(value));
				}
			}
		}
		return params;
	}

	public long getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(long maxTime) {
		this.maxTime = maxTime;
	}

	public boolean isFormat() {
		return format;
	}

	public void setFormat(boolean format) {
		this.format = format;
	}
}
