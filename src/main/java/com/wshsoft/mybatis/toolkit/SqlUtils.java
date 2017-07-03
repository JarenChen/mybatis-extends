package com.wshsoft.mybatis.toolkit;


import com.wshsoft.mybatis.enums.SqlLike;
import com.wshsoft.mybatis.plugins.pagination.Pagination;
import com.wshsoft.mybatis.plugins.pagination.optimize.JsqlParserCountOptimize;
import com.wshsoft.mybatis.parser.AbstractSqlParser;
import com.wshsoft.mybatis.parser.SqlInfo;

/**
 * <p>
 * SqlUtils工具类
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-11-13
 */
public class SqlUtils {

    private final static SqlFormatter sqlFormatter = new SqlFormatter();
    public final static String SQL_BASE_COUNT = "SELECT COUNT(1) FROM ( %s ) TOTAL";
    public static AbstractSqlParser COUNT_SQL_PARSER = null;

    /**
     * <p>
     * 获取CountOptimize
     * </p>
     *
     * @param sqlParser   Count SQL 解析类
     * @param originalSql 需要计算Count SQL
     * @return SqlInfo
     */
    public static SqlInfo getCountOptimize(AbstractSqlParser sqlParser, String originalSql) {
        // COUNT SQL 解析器
        if (null == COUNT_SQL_PARSER) {
            if (null != sqlParser) {
                // 用户自定义 COUNT SQL 解析
                COUNT_SQL_PARSER = sqlParser;
            } else {
                // 默认 JsqlParser 优化 COUNT
                COUNT_SQL_PARSER = new JsqlParserCountOptimize();
            }
        }
        return COUNT_SQL_PARSER.optimizeSql(originalSql);
    }

	/**
	 * 查询SQL拼接Order By
	 * 
	 * @param originalSql
	 *            需要拼接的SQL
	 * @param page
	 *            page对象
	 * @param orderBy
	 *            是否需要拼接Order By
	 * @return
	 */
	public static String concatOrderBy(String originalSql, Pagination page, boolean orderBy) {
		if (orderBy && StringUtils.isNotEmpty(page.getOrderByField()) && page.isOpenSort()) {
			StringBuilder buildSql = new StringBuilder(originalSql);
			buildSql.append(" ORDER BY ").append(page.getOrderByField());
			buildSql.append(page.isAsc() ? " ASC " : " DESC ");
			return buildSql.toString();
		}
		return originalSql;
	}

	/**
	 * 格式sql
	 * 
	 * @param boundSql
	 * @param format
	 * @return
	 */
	public static String sqlFormat(String boundSql, boolean format) {
		if (format) {
			return sqlFormatter.format(boundSql);
		} else {
			return boundSql.replaceAll("[\\s]+", " ");
		}
	}

	/**
	 * <p>
	 * 用%连接like
	 * </p>
	 *
	 * @param str
	 *            原字符串
	 * @return
	 */
	public static String concatLike(String str, SqlLike type) {
		StringBuilder builder = new StringBuilder(str.length() + 3);
		switch (type) {
		case LEFT:
			builder.append("%").append(str);
			break;
		case RIGHT:
			builder.append(str).append("%");
			break;
		case CUSTOM:
			builder.append(str);
			break;
		default:
			builder.append("%").append(str).append("%");
		}
		return builder.toString();
	}

}
