package com.wshsoft.mybatis.plugins.parser.tenant;

import java.util.List;

import com.wshsoft.mybatis.exceptions.MybatisExtendsException;
import com.wshsoft.mybatis.plugins.parser.AbstractJsqlParser;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.LateralSubSelect;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubJoin;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.ValuesList;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.statement.update.Update;

/**
 * <p>
 * 租户 SQL 解析（ TenantId 行级 ）
 * </p>
 *
 * @author Carry xie
 * @since 2017-09-01
 */
public class TenantSqlParser extends AbstractJsqlParser {

	private TenantHandler tenantHandler;

	/**
	 * select 语句处理
	 */
	@Override
	public void processSelectBody(SelectBody selectBody) {
		if (selectBody instanceof PlainSelect) {
			processPlainSelect((PlainSelect) selectBody);
		} else if (selectBody instanceof WithItem) {
			WithItem withItem = (WithItem) selectBody;
			if (withItem.getSelectBody() != null) {
				processSelectBody(withItem.getSelectBody());
			}
		} else {
			SetOperationList operationList = (SetOperationList) selectBody;
			if (operationList.getSelects() != null && operationList.getSelects().size() > 0) {
				List<SelectBody> plainSelects = operationList.getSelects();
				for (SelectBody plainSelect : plainSelects) {
					processSelectBody(plainSelect);
				}
			}
		}
	}

	/**
	 * <p>
	 * insert 语句处理
	 * </p>
	 */
	@Override
	public void processInsert(Insert insert) {
		if (this.tenantHandler.doTableFilter(insert.getTable().getName())) {
			// 过滤退出执行
			return;
		}
		insert.getColumns().add(new Column(this.tenantHandler.getTenantIdColumn()));
		if (insert.getSelect() != null) {
			processPlainSelect((PlainSelect) insert.getSelect().getSelectBody(), true);
		} else if (insert.getItemsList() != null) {
			((ExpressionList) insert.getItemsList()).getExpressions().add(tenantHandler.getTenantId());
		} else {
			throw new MybatisExtendsException(
					"Failed to process multiple-table update, please exclude the tableName or statementId");
		}
	}

	/**
	 * <p>
	 * update 语句处理
	 * </p>
	 */
	@Override
	public void processUpdate(Update update) {
		List<Table> tableList = update.getTables();
		if (null == tableList || tableList.size() >= 2) {
			throw new MybatisExtendsException(
					"Failed to process multiple-table update, please exclude the statementId");
		}
		Table table = tableList.get(0);
		if (this.tenantHandler.doTableFilter(table.getName())) {
			// 过滤退出执行
			return;
		}
		update.setWhere(this.andExpression(table, update.getWhere()));
	}

	/**
	 * <p>
	 * delete 语句处理
	 * </p>
	 */
	@Override
	public void processDelete(Delete delete) {
		if (this.tenantHandler.doTableFilter(delete.getTable().getName())) {
			// 过滤退出执行
			return;
		}
		delete.setWhere(this.andExpression(delete.getTable(), delete.getWhere()));
	}

	/**
	 * <p>
	 * delete update 语句 where 处理
	 * </p>
	 */
	protected BinaryExpression andExpression(Table table, Expression where) {
		// 获得where条件表达式
		EqualsTo equalsTo = new EqualsTo();
		if (null != where) {
			equalsTo.setLeftExpression(new Column(this.tenantHandler.getTenantIdColumn()));
			equalsTo.setRightExpression(tenantHandler.getTenantId());
			return new AndExpression(equalsTo, where);
		}
		equalsTo.setLeftExpression(this.getAliasColumn(table));
		equalsTo.setRightExpression(tenantHandler.getTenantId());
		return equalsTo;
	}

	/**
	 * <p>
	 * 处理 PlainSelect
	 * </p>
	 */
	protected void processPlainSelect(PlainSelect plainSelect) {
		processPlainSelect(plainSelect, false);
	}

	/**
	 * <p>
	 * 处理 PlainSelect
	 * </p>
	 *
	 * @param plainSelect
	 * @param addColumn
	 *            是否添加租户列,insert into select语句中需要
	 */
	protected void processPlainSelect(PlainSelect plainSelect, boolean addColumn) {
		FromItem fromItem = plainSelect.getFromItem();
		if (fromItem instanceof Table) {
			Table fromTable = (Table) fromItem;
			if (this.tenantHandler.doTableFilter(fromTable.getName())) {
				// 过滤退出执行
				return;
			}
			plainSelect.setWhere(builderExpression(plainSelect.getWhere(), fromTable));
			if (addColumn) {
				plainSelect.getSelectItems()
						.add(new SelectExpressionItem(new Column(this.tenantHandler.getTenantIdColumn())));
			}
		} else {
			processFromItem(fromItem);
		}
		List<Join> joins = plainSelect.getJoins();
		if (joins != null && joins.size() > 0) {
			for (Join join : joins) {
				processJoin(join);
				processFromItem(join.getRightItem());
			}
		}
	}

	/**
	 * 处理子查询等
	 */
	protected void processFromItem(FromItem fromItem) {
		if (fromItem instanceof SubJoin) {
			SubJoin subJoin = (SubJoin) fromItem;
			if (subJoin.getJoin() != null) {
				processJoin(subJoin.getJoin());
			}
			if (subJoin.getLeft() != null) {
				processFromItem(subJoin.getLeft());
			}
		} else if (fromItem instanceof SubSelect) {
			SubSelect subSelect = (SubSelect) fromItem;
			if (subSelect.getSelectBody() != null) {
				processSelectBody(subSelect.getSelectBody());
			}
		} else if (fromItem instanceof ValuesList) {
			logger.debug("Perform a subquery, if you do not give us feedback");
		} else if (fromItem instanceof LateralSubSelect) {
			LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
			if (lateralSubSelect.getSubSelect() != null) {
				SubSelect subSelect = lateralSubSelect.getSubSelect();
				if (subSelect.getSelectBody() != null) {
					processSelectBody(subSelect.getSelectBody());
				}
			}
		}
	}

	/**
	 * 处理联接语句
	 */
	protected void processJoin(Join join) {
		if (join.getRightItem() instanceof Table) {
			Table fromTable = (Table) join.getRightItem();
			if (this.tenantHandler.doTableFilter(fromTable.getName())) {
				// 过滤退出执行
				return;
			}
			join.setOnExpression(builderExpression(join.getOnExpression(), fromTable));
		}
	}

	/**
	 * 处理条件
	 */
	protected Expression builderExpression(Expression expression, Table table) {
		// 生成字段名
		EqualsTo equalsTo = new EqualsTo();
		equalsTo.setLeftExpression(this.getAliasColumn(table));
		equalsTo.setRightExpression(tenantHandler.getTenantId());
		// 加入判断防止条件为空时生成 "and null" 导致查询结果为空
		if (expression == null) {
			return equalsTo;
		} else {
			if (expression instanceof BinaryExpression) {
				BinaryExpression binaryExpression = (BinaryExpression) expression;
				if (binaryExpression.getLeftExpression() instanceof FromItem) {
					processFromItem((FromItem) binaryExpression.getLeftExpression());
				}
				if (binaryExpression.getRightExpression() instanceof FromItem) {
					processFromItem((FromItem) binaryExpression.getRightExpression());
				}
			}
			return new AndExpression(equalsTo, expression);
		}
	}

	/**
	 * <p>
	 * 字段是否添加别名设置
	 * </p>
	 *
	 * @param table
	 *            表对象
	 * @return 字段
	 */
	protected Column getAliasColumn(Table table) {
		if (null == table.getAlias()) {
			return new Column(this.tenantHandler.getTenantIdColumn());
		}
		StringBuilder column = new StringBuilder();
		column.append(table.getAlias().getName());
		column.append(".");
		column.append(this.tenantHandler.getTenantIdColumn());
		return new Column(column.toString());
	}

	public TenantHandler getTenantHandler() {
		return tenantHandler;
	}

	public void setTenantHandler(TenantHandler tenantHandler) {
		this.tenantHandler = tenantHandler;
	}
}