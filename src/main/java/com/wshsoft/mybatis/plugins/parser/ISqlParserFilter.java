package com.wshsoft.mybatis.plugins.parser;

import org.apache.ibatis.reflection.MetaObject;

/**
 * <p>
 * SQL 解析过滤器
 * </p>
 *
 * @author Carry xie
 * @Date 2017-09-02
 */
public interface ISqlParserFilter {

	boolean doFilter(MetaObject metaObject);

}
