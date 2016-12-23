package com.wshsoft.mybatis.generator.config.rules;

/**
 * 数据库类型定义
 *
 * @author Carry xie
 * @since 2016/8/30
 */
public enum DbType {

	MYSQL("mysql"), ORACLE("oracle");

	private String value;

	DbType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
