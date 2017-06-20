package com.wshsoft.mybatis.toolkit;

import java.util.ArrayList;
import java.util.List;

import com.wshsoft.mybatis.entity.CountOptimize;

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

/**
 * <p>
 * JsqlParserUtils工具类
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-11-30
 */
public class JsqlParserUtils {

    private static final List<SelectItem> countSelectItem = countSelectItem();

    /**
     * <p>
     * jsqlparser方式获取select的count语句
     * </p>
     *
     * @param originalSql selectSQL
     * @return
     */
    public static CountOptimize jsqlparserCount(CountOptimize countOptimize, String originalSql) {
        try {
            Select selectStatement = (Select) CCJSqlParserUtil.parse(originalSql);
            PlainSelect plainSelect = (PlainSelect) selectStatement.getSelectBody();
            Distinct distinct = plainSelect.getDistinct();
            List<Expression> groupBy = plainSelect.getGroupByColumnReferences();
            List<OrderByElement> orderBy = plainSelect.getOrderByElements();

            // 添加包含groupBy 不去除orderBy
            if (CollectionUtils.isEmpty(groupBy) && CollectionUtils.isNotEmpty(orderBy)) {
                plainSelect.setOrderByElements(null);
                countOptimize.setOrderBy(false);
            }

            // 包含 distinct、groupBy不优化
            if (distinct != null || CollectionUtils.isNotEmpty(groupBy)) {
                countOptimize.setCountSQL(String.format(SqlUtils.SQL_BASE_COUNT, selectStatement.toString()));
                return countOptimize;
            }

            // 优化 SQL
            plainSelect.setSelectItems(countSelectItem);
            countOptimize.setCountSQL(selectStatement.toString());
            return countOptimize;
        } catch (Throwable e) {
            // 无法优化使用原 SQL
            countOptimize.setCountSQL(String.format(SqlUtils.SQL_BASE_COUNT, originalSql));
            return countOptimize;
        }
    }

    /**
     * <p>
     * 获取jsqlparser中count的SelectItem
     * </p>
     *
     * @return
     */
    private static List<SelectItem> countSelectItem() {
        Function function = new Function();
        function.setName("COUNT");
        List<Expression> expressions = new ArrayList<>();
        LongValue longValue = new LongValue(1);
        ExpressionList expressionList = new ExpressionList();
        expressions.add(longValue);
        expressionList.setExpressions(expressions);
        function.setParameters(expressionList);
        List<SelectItem> selectItems = new ArrayList<>();
        SelectExpressionItem selectExpressionItem = new SelectExpressionItem(function);
        selectItems.add(selectExpressionItem);
        return selectItems;
    }
}
