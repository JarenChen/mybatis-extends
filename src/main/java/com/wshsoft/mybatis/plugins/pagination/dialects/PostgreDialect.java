package com.wshsoft.mybatis.plugins.pagination.dialects;

import com.wshsoft.mybatis.plugins.pagination.IDialect;

/**
 * <p>
 * Postgre 数据库分页语句组装实现
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-01-23
 */
public class PostgreDialect implements IDialect {

    public static final PostgreDialect INSTANCE = new PostgreDialect();

    @Override
    public String buildPaginationSql(String originalSql, int offset, int limit) {
		StringBuilder sql = new StringBuilder(originalSql);
        sql.append(" limit ").append(limit).append(" offset ").append(offset);
        return sql.toString();
    }

}
