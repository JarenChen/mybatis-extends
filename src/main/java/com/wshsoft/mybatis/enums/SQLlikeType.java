package com.wshsoft.mybatis.enums;


/**
 * <p>
 * SQL like 枚举
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-12-4
 */
public enum SQLlikeType {
	/**
	 * LEFT
	 */
	LEFT("left", "左边"),
	/**
	 * RIGHT
	 */
	RIGHT("right", "右边"),
	/**
	 * DEFAULT
	 */
	DEFAULT("default", "两边");
	/** 主键 */
	private final String type;

	/** 描述 */
	private final String desc;

	SQLlikeType(final String type, final String desc) {
		this.type = type;
		this.desc = desc;
	}
}
