package com.wshsoft.mybatis.plugins.parser.tenant;

import com.wshsoft.mybatis.plugins.parser.AbstractJsqlParser;

import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.update.Update;

/**
 * <p>
 * 租户 SQL 解析（ Schema 表级 ）
 * </p>
 *
 * @author Carry xie
 * @since 2017-09-01
 */
public class TenantSchemaSqlParser extends AbstractJsqlParser {

    private TenantSchemaHandler tenantSchemaHandler;

    @Override
    public void processInsert(Insert insert) {

    }

    @Override
    public void processDelete(Delete delete) {

    }

    @Override
    public void processUpdate(Update update) {

    }

    @Override
    public void processSelectBody(SelectBody selectBody) {

    }

    public TenantSchemaHandler getTenantSchemaHandler() {
        return tenantSchemaHandler;
    }

    public void setTenantSchemaHandler(TenantSchemaHandler tenantSchemaHandler) {
        this.tenantSchemaHandler = tenantSchemaHandler;
    }
}
