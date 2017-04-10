package com.wshsoft.mybatis.plugins;

import java.sql.Connection;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.wshsoft.mybatis.entity.CountOptimize;
import com.wshsoft.mybatis.plugins.pagination.DialectFactory;
import com.wshsoft.mybatis.plugins.pagination.Pagination;
import com.wshsoft.mybatis.toolkit.PluginUtils;
import com.wshsoft.mybatis.toolkit.SqlUtils;
import com.wshsoft.mybatis.toolkit.StringUtils;

/**
 * <p>
 * 缓存分页拦截器
 * </p>
 *
 * @author Carry xie
 * @Date 2016-01-23
 */
@Intercepts({
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }),
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class CachePaginationInterceptor extends PaginationInterceptor implements Interceptor {
	/* Count优化方式 */
	private String optimizeType = "default";
	/* 方言类型 */
	private String dialectType;
	/* 方言实现类 */
	private String dialectClazz;

	/**
	 * Physical Pagination Interceptor for all the queries with parameter
	 * {@link org.apache.ibatis.session.RowBounds}
	 */
	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		Object target = invocation.getTarget();
		if (target instanceof StatementHandler) {
			StatementHandler statementHandler = (StatementHandler) PluginUtils.realTarget(invocation.getTarget());
			MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
			RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");

			if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
				return invocation.proceed();
			}
			BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
			String originalSql = boundSql.getSql();

			if (rowBounds instanceof Pagination) {
				Pagination page = (Pagination) rowBounds;
				boolean orderBy = true;
				if (page.isSearchCount()) {
					CountOptimize countOptimize = SqlUtils.getCountOptimize(originalSql, optimizeType, dialectType,
							page.isOptimizeCount());
					orderBy = countOptimize.isOrderBy();
				}
				String buildSql = SqlUtils.concatOrderBy(originalSql, page, orderBy);
				originalSql = DialectFactory.buildPaginationSql(page, buildSql, dialectType, dialectClazz);
			} else {
				// support physical Pagination for RowBounds
				originalSql = DialectFactory.buildPaginationSql(rowBounds, originalSql, dialectType, dialectClazz);
			}

			metaStatementHandler.setValue("delegate.boundSql.sql", originalSql);
			metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
			metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);
		} else {
			RowBounds rowBounds = (RowBounds) invocation.getArgs()[2];
			if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
				return invocation.proceed();
			}
			MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
			Executor executor = (Executor) invocation.getTarget();
			Connection connection = executor.getTransaction().getConnection();
			Object parameterObject = invocation.getArgs()[1];
			BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
			String originalSql = boundSql.getSql();
			if (rowBounds instanceof Pagination) {
				Pagination page = (Pagination) rowBounds;
				if (page.isSearchCount()) {
					CountOptimize countOptimize = SqlUtils.getCountOptimize(originalSql, optimizeType, dialectType,
							page.isOptimizeCount());
					super.queryTotal(countOptimize.getCountSQL(), mappedStatement, boundSql, page, connection);
					if (page.getTotal() <= 0) {
						return invocation.proceed();
					}
				}
			}
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		}
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

	@Override
	public void setProperties(Properties prop) {
		String dialectType = prop.getProperty("dialectType");
		String dialectClazz = prop.getProperty("dialectClazz");
		if (StringUtils.isNotEmpty(dialectType)) {
			this.dialectType = dialectType;
		}
		if (StringUtils.isNotEmpty(dialectClazz)) {
			this.dialectClazz = dialectClazz;
		}
	}

	@Override
	public void setDialectType(String dialectType) {
		this.dialectType = dialectType;
	}

	@Override
	public void setOptimizeType(String optimizeType) {
		this.optimizeType = optimizeType;
	}

}
