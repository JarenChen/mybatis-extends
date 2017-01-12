package com.wshsoft.mybatis.generator.config.rules;

/**
 * <p>
 * 表字段类型
 * </p>
 * 
 * @author Carry xie
 * @since 2017-01-11
 */
public enum DbColumnType {
	STRING("String", null),
	LONG("Long", null),
	INTEGER("Integer", null),
	FLOAT("Float", null),
	DOUBLE("Double", null),
	BOOLEAN("Boolean", null),
	BYTE_ARRAY("byte[]", null),
	OBJECT("Object", null),
	DATE("Date", "java.util.Date"),
	BIG_DECIMAL("BigDecimal", "java.math.BigDecimal");

	/** 类型 */
	private final String type;

	/** 包路径 */
	private final String pkg;

	DbColumnType(final String type, final String pkg) {
		this.type = type;
		this.pkg = pkg;
	}

	public String getType() {
		return this.type;
	}

	public String getPkg() {
		return this.pkg;
	}

}
