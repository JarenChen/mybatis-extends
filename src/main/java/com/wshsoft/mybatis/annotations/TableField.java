package com.wshsoft.mybatis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.wshsoft.mybatis.enums.FieldFill;
import com.wshsoft.mybatis.enums.FieldStrategy;

/**
 * <p>
 * 表字段标识
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-09-09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableField {

	/**
	 * <p>
	 * 字段值（驼峰命名方式，该值可无）
	 * </p>
	 */
	String value() default "";

	/**
	 * <p>
	 * 当该Field为类对象时, 可使用#{对象.属性}来映射到数据表.
	 * </p>
	 * <p>
	 * 支持：@TableField(el = "role, jdbcType=BIGINT)<br>
	 * 支持：@TableField(el =
	 * "role, typeHandler=com.wshsoft.xx.typehandler.PhoneTypeHandler")
	 * </p>
	 */
	String el() default "";

	/**
	 * <p>
	 * 是否为数据库表字段
	 * </p>
	 * <p>
	 * 默认 true 存在，false 不存在
	 * </p>
	 */
	boolean exist() default true;

	/**
	 * <p>
	 * 字段验证策略
	 * </p>
	 * <p>
	 * 默认 非 null 判断
	 * </p>
	 */
	FieldStrategy strategy() default FieldStrategy.NOT_NULL;

	/**
	 * <p>
	 * 字段自动填充策略
	 * </p>
	 */
	FieldFill fill() default FieldFill.DEFAULT;
}
