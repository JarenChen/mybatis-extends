package com.wshsoft.mybatis.generator.config.po;

import com.wshsoft.mybatis.generator.config.rules.DbColumnType;

/**
 * <p>
 * 表字段信息
 * </p>
 * 
 * @author Carry xie
 * @since 2016-8-30
 */
public class TableField {
	private boolean convert;
	private boolean keyFlag;
	private String name;
	private String type;
	private String propertyName;
	private DbColumnType columnType;
	private String comment;

	public boolean isConvert() {
		return convert;
	}

	public void setConvert(boolean convert) {
		this.convert = convert;
	}

	public boolean isKeyFlag() {
		return keyFlag;
	}

	public void setKeyFlag(boolean keyFlag) {
		this.keyFlag = keyFlag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public DbColumnType getColumnType() {
		return columnType;
	}

	public void setColumnType(DbColumnType columnType) {
		this.columnType = columnType;
	}

	public String getPropertyType() {
		if (null != columnType) {
			return columnType.getType();
		}
		return null;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getCapitalName() {
		return propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
	}

}
