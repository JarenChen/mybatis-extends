package com.wshsoft.mybatis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 序列主键策略 oracle
 * </p>
 *
 * @author Carry xie
 * @since 2017.4.20
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface KeySequence {

	/*
	 * <p> 序列名 </p>
	 */
	String value() default "";

	/*
	 * <p> id的类型 </p>
	 */
	Class clazz() default Long.class;

}
