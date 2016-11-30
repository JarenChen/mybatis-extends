package com.wshsoft.mybatis.toolkit;

import com.wshsoft.mybatis.plugins.SQLFormatter;
import com.wshsoft.mybatis.plugins.entity.CountOptimize;
import com.wshsoft.mybatis.plugins.entity.Optimize;
import com.wshsoft.mybatis.plugins.pagination.Pagination;

/**
 * <p>
 * SqlUtils工具类
 * </p>
 *
 * @author Carry xie
 * @Date 2016-11-13
 */
public class SqlUtils {
	private final static SQLFormatter sqlFormatter = new SQLFormatter();
	public static final String SQL_BASE_COUNT = "SELECT COUNT(1) FROM ( %s )";

	/**
	 * 获取CountOptimize
	 * 
	 * @param originalSql
	 *            需要计算Count SQL
	 * @param optimizeType
	 *            count优化方式
	 * @param isOptimizeCount
	 *            是否需要优化Count
	 * @return CountOptimize
	 */
	public static CountOptimize getCountOptimize(String originalSql, String optimizeType, String dialectType,
			boolean isOptimizeCount) {
		CountOptimize countOptimize = CountOptimize.newInstance();
		if (isOptimizeCount) {
			String tempSql = originalSql.replaceAll("(?i)ORDER[\\s]+BY", "ORDER BY").replaceAll("(?i)GROUP[\\s]+BY", "GROUP BY");
			String indexOfSql = tempSql.toUpperCase();
			// 有排序情况
			int orderByIndex = indexOfSql.lastIndexOf("ORDER BY");
			// 只针对 ALI_DRUID DEFAULT 这2种情况
			if (orderByIndex > -1) {
				countOptimize.setOrderBy(false);
			}
			Optimize opType = Optimize.getOptimizeType(optimizeType);
			switch (opType) {
			case ALI_DRUID:
				/**
				 * 调用ali druid方式 插件dbType一定要设置为小写与JdbcConstants保持一致
				 * 
				 * @see com.alibaba.druid.util.JdbcConstants
				 */
				String aliCountSql = DruidUtils.count(originalSql, dialectType);
				countOptimize.setCountSQL(aliCountSql);
				break;
			case JSQLPARSER:
				/**
				 * 调用JsqlParser方式
				 */
				JsqlParserUtils.jsqlparserCount(countOptimize, originalSql);
				break;
			default:
				StringBuffer countSql = new StringBuffer("SELECT COUNT(1) AS TOTAL ");
				boolean optimize = false;
				if (!indexOfSql.contains("DISTINCT") && !indexOfSql.contains("GROUP BY")) {
					int formIndex = indexOfSql.indexOf("FROM");
					if (formIndex > -1) {
						if (orderByIndex > -1) {
							tempSql = tempSql.substring(0, orderByIndex);
							countSql.append(tempSql.substring(formIndex));
							// 无排序情况
						} else {
							countSql.append(tempSql.substring(formIndex));
						}
						// 执行优化
						optimize = true;
					}
				}
				if (!optimize) {
					// 无优化SQL
					countSql.append("FROM (").append(originalSql).append(") A");
				}
				countOptimize.setCountSQL(countSql.toString());
			}

		}
		return countOptimize;
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
		if (orderBy && StringUtils.isNotEmpty(page.getOrderByField())) {
			StringBuffer buildSql = new StringBuffer(originalSql);
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

}