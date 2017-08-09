package com.wshsoft.mybatis.parser;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;

/**
 * <p>
 * 抽象 SQL 解析类
 * </p>
 *
 * @author Carry xie
 * @Date 2017-06-20
 */
public abstract class AbstractSqlParser {

    // 日志
    protected final Log logger = LogFactory.getLog(this.getClass());

    /**
     * <p>
     * 获取优化 SQL 方法
     * </p>
     *
     * @param sql    SQL 语句
     * @return SQL 信息
     */
    public abstract SqlInfo optimizeSql(String sql);

}
