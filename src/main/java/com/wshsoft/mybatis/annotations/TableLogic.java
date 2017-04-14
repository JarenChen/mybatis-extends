package com.wshsoft.mybatis.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 表字段逻辑处理注解（逻辑删除）
 * </p>
 *
 * @author Carry xie
 * @since 2017-04-09
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableLogic {

	/**
	 * <p>
	 * 默认逻辑未删除值（该值可无、会自动获取全局配置）
	 * </p>
	 */
	String value() default "";

	/**
	 * <p>
	 * 默认逻辑删除值（该值可无、会自动获取全局配置）
	 * </p>
	 */
	String delval() default "";

}
