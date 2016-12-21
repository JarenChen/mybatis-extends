package com.wshsoft.mybatis.generator.config.po;

import com.wshsoft.mybatis.generator.config.StrategyConfig;

/**
 * <p>
 * 表字段信息
 *  </p>
 *  
 * @author Carry xie
 * @since 2016-8-30
 */
public class TableField {
	private boolean keyFlag;
	private String name;
	private String type;
	private String propertyName;
	private String propertyType;
	private String comment;

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

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isConvert() {
		if (StrategyConfig.DB_COLUMN_UNDERLINE) {
			return false;
		}
		return !name.equals(propertyName);
	}

	public String getCapitalName() {
		return propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
	}

}
