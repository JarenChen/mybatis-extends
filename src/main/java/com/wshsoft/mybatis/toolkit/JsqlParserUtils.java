package com.wshsoft.mybatis.toolkit;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.Distinct;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;

import java.util.ArrayList;
import java.util.List;

import com.wshsoft.mybatis.plugins.entity.CountOptimize;

/**
 * <p>
 * JsqlParserUtils工具类
 * </p>
 *
 * @author Carry xie
 * @Date 2016-11-30
 */
public class JsqlParserUtils {
	private static List<SelectItem> countSelectItem = null;

	/**
	 * jsqlparser方式获取select的count语句
	 *
	 * @param originalSql
	 *            selectSQL
	 * @return
	 */
	public static CountOptimize jsqlparserCount(CountOptimize countOptimize, String originalSql) {
		String sqlCount;
		try {
			Select selectStatement = (Select) CCJSqlParserUtil.parse(originalSql);
			PlainSelect plainSelect = (PlainSelect) selectStatement.getSelectBody();
			Distinct distinct = plainSelect.getDistinct();
			List<Expression> groupBy = plainSelect.getGroupByColumnReferences();
			// 包含 distinct、groupBy不优化
			if (distinct != null || CollectionUtil.isNotEmpty(groupBy)) {
				sqlCount = String.format(SqlUtils.SQL_BASE_COUNT, originalSql);
			}
			// 优化Order by
			List<OrderByElement> orderBy = plainSelect.getOrderByElements();
			if (CollectionUtil.isNotEmpty(orderBy)) {
				plainSelect.setOrderByElements(null);
				countOptimize.setOrderBy(false);
			}
			List<SelectItem> selectCount = countSelectItem();
			plainSelect.setSelectItems(selectCount);
			sqlCount = selectStatement.toString();
		} catch (Exception e) {
			sqlCount = String.format(SqlUtils.SQL_BASE_COUNT, originalSql);
		}
		countOptimize.setCountSQL(sqlCount);
		return countOptimize;
	}

	/**
	 * 获取jsqlparser中count的SelectItem
	 *
	 * @return
	 */
	private static List<SelectItem> countSelectItem() {
		if (CollectionUtil.isNotEmpty(countSelectItem)) {
			return countSelectItem;
		}
		Function function = new Function();
		function.setName("COUNT");
		List<Expression> expressions = new ArrayList<Expression>();
		LongValue longValue = new LongValue(1);
		ExpressionList expressionList = new ExpressionList();
		expressions.add(longValue);
		expressionList.setExpressions(expressions);
		function.setParameters(expressionList);
		countSelectItem = new ArrayList<SelectItem>();
		SelectExpressionItem selectExpressionItem = new SelectExpressionItem(function);
		countSelectItem.add(selectExpressionItem);
		return countSelectItem;
	}
}