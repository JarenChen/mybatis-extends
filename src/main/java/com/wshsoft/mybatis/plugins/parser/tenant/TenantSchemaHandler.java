package com.wshsoft.mybatis.plugins.parser.tenant;

/**
 * <p>
 * 租户处理器（ Schema 表级 ）
 * </p>
 *
 * @author Carry xie
 * @since 2017-08-31
 */
public interface TenantSchemaHandler {

	String getTenantSchema();

	boolean doTableFilter(String tableName);
}
