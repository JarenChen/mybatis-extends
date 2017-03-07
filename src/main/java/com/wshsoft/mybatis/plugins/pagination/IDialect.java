package com.wshsoft.mybatis.plugins.pagination;

/**
 * <p>
 * 数据库 分页语句组装接口
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-01-23
 */
public interface IDialect {

    /**
     * 组装分页语句
     * 
     * @param originalSql 原始语句
     * @param offset 偏移量
     * @param limit 界限
     * @return 分页语句
     */
    public String buildPaginationSql(String originalSql, int offset, int limit);
}
