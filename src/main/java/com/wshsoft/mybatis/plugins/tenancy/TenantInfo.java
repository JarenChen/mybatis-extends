package com.wshsoft.mybatis.plugins.tenancy;

import java.util.Properties;

/**
 * <p>
 * 租户信息
 * </p>
 *
 * @author Carry xie
 * @since 2017-06-20
 */
public interface TenantInfo {

    TenantInfo setHandlerConfig(Properties properties);

    String getTenantId();

    String getTenantIdColumn();

    TenantInfo setTenantIdColumn(String tenantIdColumn);

}
