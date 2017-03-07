package com.wshsoft.mybatis.plugins.pagination.dialects;

import com.wshsoft.mybatis.plugins.pagination.IDialect;
import com.wshsoft.mybatis.toolkit.StringUtils;

/**
 * <p>
 * SQLServer 2005 数据库分页方言
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-11-10
 */
public class SQLServer2005Dialect implements IDialect {

    public static final SQLServer2005Dialect INSTANCE = new SQLServer2005Dialect();

    private static String getOrderByPart(String sql) {
        String loweredString = sql.toLowerCase();
        int orderByIndex = loweredString.indexOf("order by");
        if (orderByIndex != -1) {
            return sql.substring(orderByIndex);
        } else {
            return "";
        }
    }

    @Override
    public String buildPaginationSql(String originalSql, int offset, int limit) {
        StringBuilder pagingBuilder = new StringBuilder();
        String orderby = getOrderByPart(originalSql);
        String distinctStr = "";

        String loweredString = originalSql.toLowerCase();
        String sqlPartString = originalSql;
        if (loweredString.trim().startsWith("select")) {
            int index = 6;
            if (loweredString.startsWith("select distinct")) {
                distinctStr = "DISTINCT ";
                index = 15;
            }
            sqlPartString = sqlPartString.substring(index);
        }
        pagingBuilder.append(sqlPartString);

        // if no ORDER BY is specified use fake ORDER BY field to avoid errors
        if (StringUtils.isEmpty(orderby)) {
            orderby = "ORDER BY CURRENT_TIMESTAMP";
        }

        StringBuilder sql = new StringBuilder();
        sql.append("WITH query AS (SELECT ").append(distinctStr).append("TOP 100 PERCENT ")
                .append(" ROW_NUMBER() OVER (").append(orderby).append(") as __row_number__, ").append(pagingBuilder)
                .append(") SELECT * FROM query WHERE __row_number__ BETWEEN ").append(offset).append(" AND ")
                .append(offset + limit).append(" ORDER BY __row_number__");
        return sql.toString();
    }

}
