package com.wshsoft.mybatis.plugins.parser;

/**
 * <p>
 * Sql Info
 * </p>
 *
 * @author Carry xie
 * @Date 2017-06-20
 */
public class SqlInfo {

	private String sql;// SQL 内容
	private boolean orderBy = true;// 是否排序

	public static SqlInfo newInstance() {
		return new SqlInfo();
	}

	public String getSql() {
		return sql;
	}

	public SqlInfo setSql(String sql) {
		this.sql = sql;
		return this;
	}

	public boolean isOrderBy() {
		return orderBy;
	}

	public SqlInfo setOrderBy(boolean orderBy) {
		this.orderBy = orderBy;
		return this;
	}
}
