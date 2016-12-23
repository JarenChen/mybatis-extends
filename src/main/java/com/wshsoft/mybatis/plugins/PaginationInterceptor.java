package com.wshsoft.mybatis.plugins;

import com.wshsoft.mybatis.MybatisDefaultParameterHandler;
import com.wshsoft.mybatis.entity.CountOptimize;
import com.wshsoft.mybatis.exceptions.MybatisExtendsException;
import com.wshsoft.mybatis.plugins.pagination.DialectFactory;
import com.wshsoft.mybatis.plugins.pagination.IDialect;
import com.wshsoft.mybatis.plugins.pagination.Pagination;
import com.wshsoft.mybatis.toolkit.IOUtils;
import com.wshsoft.mybatis.toolkit.SqlUtils;
import com.wshsoft.mybatis.toolkit.StringUtils;
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
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * <p>
 * 分页拦截器
 * </p>
 *
 * @author Carry xie
 * @Date 2016-01-23
 */
@Intercepts({
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class,
				ResultHandler.class }),
		@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
public class PaginationInterceptor implements Interceptor {

	/* 溢出总页数，设置第一页 */
	private boolean overflowCurrent = false;
	/* Count优化方式 */
	private String optimizeType = "default";
	/* 方言类型 */
	private String dialectType;
	/* 方言实现类 */
	private String dialectClazz;

	public Object intercept(Invocation invocation) throws Throwable {

		Object target = invocation.getTarget();
		if (target instanceof StatementHandler) {
			StatementHandler statementHandler = (StatementHandler) target;
			MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
			RowBounds rowBounds = (RowBounds) metaStatementHandler.getValue("delegate.rowBounds");

			/* 不需要分页的场合 */
			if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
				return invocation.proceed();
			}

			/* 定义数据库方言 */
			IDialect dialect = getiDialect();

			/*
			 * <p> 禁用内存分页 </p> <p> 内存分页会查询所有结果出来处理（这个很吓人的），如果结果变化频繁这个数据还会不准。
			 * </p>
			 */
			BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
			String originalSql = (String) boundSql.getSql();
			metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
			metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);

			/**
			 * <p>
			 * 分页逻辑
			 * </p>
			 * <p>
			 * 查询总记录数 count
			 * </p>
			 */
			if (rowBounds instanceof Pagination) {
				Pagination page = (Pagination) rowBounds;
				boolean orderBy = true;
				if (page.isSearchCount()) {
					/*
					 * COUNT 查询，去掉 ORDER BY 优化执行 SQL
					 */
					CountOptimize countOptimize = SqlUtils.getCountOptimize(originalSql, optimizeType, dialectType,
							page.isOptimizeCount());
					orderBy = countOptimize.isOrderBy();
				}
				/* 执行 SQL */
				String buildSql = SqlUtils.concatOrderBy(originalSql, page, orderBy);
				originalSql = dialect.buildPaginationSql(buildSql, page.getOffsetCurrent(), page.getSize());
			}

			/**
			 * 查询 SQL 设置
			 */
			metaStatementHandler.setValue("delegate.boundSql.sql", originalSql);
		} else {
			MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
			Object parameterObject = null;
			RowBounds rowBounds = null;
			if (invocation.getArgs().length > 1) {
				parameterObject = invocation.getArgs()[1];
				rowBounds = (RowBounds) invocation.getArgs()[2];
			}
			/* 不需要分页的场合 */
			if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
				return invocation.proceed();
			}

			BoundSql boundSql = mappedStatement.getBoundSql(parameterObject);
			/*
			 * <p> 禁用内存分页 </p> <p> 内存分页会查询所有结果出来处理（这个很吓人的），如果结果变化频繁这个数据还会不准。
			 * </p>
			 */
			String originalSql = (String) boundSql.getSql();

			/**
			 * <p>
			 * 分页逻辑
			 * </p>
			 * <p>
			 * 查询总记录数 count
			 * </p>
			 */
			if (rowBounds instanceof Pagination) {
				Connection connection = null;
				try {
					connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
					Pagination page = (Pagination) rowBounds;
					if (page.isSearchCount()) {
						/*
						 * COUNT 查询，去掉 ORDER BY 优化执行 SQL
						 */
						CountOptimize countOptimize = SqlUtils.getCountOptimize(originalSql, optimizeType, dialectType,
								page.isOptimizeCount());
						page = this.count(countOptimize.getCountSQL(), connection, mappedStatement, boundSql, page);
						/** 总数 0 跳出执行 */
						if (page.getTotal() <= 0) {
							return invocation.proceed();
						}
					}
				} finally {
					IOUtils.closeQuietly(connection);
				}
			}
		}

		return invocation.proceed();

	}

	/**
	 * 获取数据库方言
	 *
	 * @return
	 * @throws Exception
	 */
	private IDialect getiDialect() throws Exception {
		IDialect dialect = null;
		if (StringUtils.isNotEmpty(dialectType)) {
			dialect = DialectFactory.getDialectByDbtype(dialectType);
		} else {
			if (StringUtils.isNotEmpty(dialectClazz)) {
				try {
					Class<?> clazz = Class.forName(dialectClazz);
					if (IDialect.class.isAssignableFrom(clazz)) {
						dialect = (IDialect) clazz.newInstance();
					}
				} catch (ClassNotFoundException e) {
					throw new MybatisExtendsException("Class :" + dialectClazz + " is not found");
				}
			}
		}
		/* 未配置方言则抛出异常 */
		if (dialect == null) {
			throw new MybatisExtendsException("The value of the dialect property in mybatis configuration.xml is not defined.");
		}
		return dialect;
	}

	/**
	 * 查询总记录条数
	 *
	 * @param sql
	 * @param connection
	 * @param mappedStatement
	 * @param boundSql
	 * @param page
	 */
	public Pagination count(String sql, Connection connection, MappedStatement mappedStatement, BoundSql boundSql, Pagination page) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sql);
			DefaultParameterHandler parameterHandler = new MybatisDefaultParameterHandler(mappedStatement,
					boundSql.getParameterObject(), boundSql);
			parameterHandler.setParameters(pstmt);
			rs = pstmt.executeQuery();
			int total = 0;
			if (rs.next()) {
				total = rs.getInt(1);
			}
			page.setTotal(total);
			/*
			 * 溢出总页数，设置第一页
			 */
			if (overflowCurrent && (page.getCurrent() > page.getPages())) {
				page = new Pagination(1, page.getSize());
				page.setTotal(total);
			}
		} catch (Exception e) {
			// ignored
		} finally {
			IOUtils.closeQuietly(pstmt, rs);
		}
		return page;
	}

	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		}
		if (target instanceof StatementHandler) {
			return Plugin.wrap(target, this);
		}
		return target;
	}

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

	public void setDialectType(String dialectType) {
		this.dialectType = dialectType;
	}

	public void setDialectClazz(String dialectClazz) {
		this.dialectClazz = dialectClazz;
	}

	public void setOverflowCurrent(boolean overflowCurrent) {
		this.overflowCurrent = overflowCurrent;
	}

	public void setOptimizeType(String optimizeType) {
		this.optimizeType = optimizeType;
	}
}
