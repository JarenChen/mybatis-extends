package com.wshsoft.mybatis.entity;

import com.wshsoft.mybatis.enums.DBType;
import com.wshsoft.mybatis.enums.FieldStrategy;
import com.wshsoft.mybatis.toolkit.SqlReservedWords;
import com.wshsoft.mybatis.toolkit.StringUtils;

/**
 * <p>
 * 数据库表字段反射信息
 * </p>
 *
 * @author Carry xie
 * @Date 2016-09-09
 */
public class TableFieldInfo {

	/**
	 * <p>
	 * 是否有存在字段名与属性名关联
	 * </p>
	 * true , false
	 */
	private boolean related;

	/**
	 * 字段名
	 */
	private String column;

	/**
	 * 属性名
	 */
	private String property;

	/**
	 * 属性表达式#{property}, 可以指定jdbcType, typeHandler等
	 */
	private String el;

	/**
	 * 字段策略【 默认，自判断 null 】
	 */
	private FieldStrategy fieldStrategy = FieldStrategy.NOT_NULL;

	public TableFieldInfo(GlobalConfiguration globalCache, String column, String property, String el, FieldStrategy fieldStrategy) {
		DBType dbType = globalCache.getDbType();
		if (globalCache.isDbColumnUnderline()) {
			/* 开启字段下划线申明 */
			this.related = true;
			this.setColumn(dbType, StringUtils.camelToUnderline(column));
		} else if (!column.equals(property)) {
			/* 没有开启下划线申明 但是column与property不等的情况下设置related为true */
			this.related = true;
			this.setColumn(dbType, column);
		} else {
			this.related = false;
			this.setColumn(dbType, column);
		}
		this.property = property;
		this.el = el;
		/*
		 * 优先使用单个字段注解，否则使用全局配置
		 */
		if (fieldStrategy != FieldStrategy.NOT_NULL) {
			this.fieldStrategy = fieldStrategy;
		} else {
			this.fieldStrategy = globalCache.getFieldStrategy();
		}
	}

	public TableFieldInfo(GlobalConfiguration globalCache, String column) {
		DBType dbType = globalCache.getDbType();
		if (globalCache.isDbColumnUnderline()) {
			/* 开启字段下划线申明 */
			this.related = true;
			this.setColumn(dbType, StringUtils.camelToUnderline(column));
		} else {
			this.related = false;
			this.setColumn(dbType, column);
		}
		this.property = column;
		this.el = column;
		this.fieldStrategy = globalCache.getFieldStrategy();
	}

	public boolean isRelated() {
		return related;
	}

	public void setRelated(boolean related) {
		this.related = related;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(DBType dbType, String column) {
		this.column = SqlReservedWords.convert(dbType, column);
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getEl() {
		return el;
	}

	public void setEl(String el) {
		this.el = el;
	}

	public FieldStrategy getFieldStrategy() {
		return fieldStrategy;
	}

	public void setFieldStrategy(FieldStrategy fieldStrategy) {
		this.fieldStrategy = fieldStrategy;
	}

}
