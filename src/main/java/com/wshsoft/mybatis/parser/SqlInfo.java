package com.wshsoft.mybatis.parser;

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

    public void setSql(String sql) {
        this.sql = sql;
    }

    public boolean isOrderBy() {
        return orderBy;
    }

    public void setOrderBy(boolean orderBy) {
        this.orderBy = orderBy;
    }
}
