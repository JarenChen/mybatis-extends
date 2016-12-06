package com.wshsoft.mybatis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.wshsoft.mybatis.enums.IdType;

/**
 * <p>
 * 表主键标识
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-01-23
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableId {

	/*
	 * <p>
	 * 字段值（驼峰命名方式，该值可无）
	 * </p>
	 */
	String value() default "";

	/*
	 * <p>
	 * 主键ID，默认 INPUT
	 * </p>
	 * {@link IdType}
	 */
	IdType type() default IdType.INPUT;
}
