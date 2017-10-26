package com.wshsoft.mybatis.test.h2.entity;

/**
 * <p>
 * 自定义父类 SuperMapper
 * </p>
 *
 * @author Carry xie
 * @date 2017-06-26
 */
public interface SuperMapper<T> extends com.wshsoft.mybatis.mapper.BaseMapper<T> {

	// 这里可以写 mapper 层公共方法、 注意！！ 不要让在 mapper 目录让 mp 扫描到!!
}
