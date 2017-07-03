package com.wshsoft.mybatis.plugins.tenancy.handler;

import java.util.Properties;

/**
 * <p>
 * 租户信息处理器
 * </p>
 *
 * @author Carry xie
 * @since 2017-06-20
 */
public interface TenancyHandler {

    /**
     * <p>
     * 配置设置
     * </p>
     *
     * @param properties mybatis Interceptor setProperties
     */
    void setConfig(Properties properties);

    /**
     * <p>
     * 按照表名处理
     * </p>
     *
     * @param table 表名
     * @return true 执行，false 不执行
     */
    boolean doTable(String table);


    /**
     * <p>
     * 按照statementId处理
     * </p>
     *
     * @param statementId mybatis statementId
     * @return true 执行，false 不执行
     */
    boolean doStatement(String statementId);

}
