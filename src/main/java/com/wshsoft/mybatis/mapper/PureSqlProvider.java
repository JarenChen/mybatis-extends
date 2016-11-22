package com.wshsoft.mybatis.mapper;

import com.wshsoft.mybatis.exceptions.MybatisExtendsException;

/**
 * <p>
 * 纯 SQL Provider
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-11-06
 */
public class PureSqlProvider {

	/**
	 * <p>
	 * 执行 SQL 语句
	 * </p>
	 * 
	 * @param sql
	 *            SQL语句
	 * @return
	 */
	public String sql(String sql) {
		if (null == sql) {
			throw new MybatisExtendsException("sql is null.");
		}
		return sql;
	}

}
