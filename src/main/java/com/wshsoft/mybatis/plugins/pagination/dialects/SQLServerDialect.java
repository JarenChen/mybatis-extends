package com.wshsoft.mybatis.plugins.pagination.dialects;

import com.wshsoft.mybatis.plugins.pagination.IDialect;

/**
 * <p>
 * SQLServer 数据库分页语句组装实现
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-03-23
 */
public class SQLServerDialect implements IDialect {

	@Override
	public String buildPaginationSql( String originalSql, int offset, int limit ) {
		StringBuffer sql = new StringBuffer(originalSql);
		sql.append(" OFFSET ").append(offset).append(" ROWS FETCH NEXT ");
		sql.append(limit).append(" ROWS ONLY");
		return sql.toString();
	}

}
