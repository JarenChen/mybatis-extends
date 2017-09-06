package com.wshsoft.mybatis.plugins.parser;

import org.apache.ibatis.reflection.MetaObject;

/**
 * <p>
 * SQL 解析接口
 * </p>
 *
 * @author Carry xie
 * @Date 2017-09-01
 */
public interface ISqlParser {

    /**
     * <p>
     * 获取优化 SQL 方法
     * </p>
     *
     * @param metaObject 元对象
     * @param sql        SQL 语句
     * @return SQL 信息
     */
    SqlInfo optimizeSql(MetaObject metaObject, String sql);

}
