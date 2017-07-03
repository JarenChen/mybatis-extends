package com.wshsoft.mybatis.generator.config.po;

import com.wshsoft.mybatis.enums.FieldFill;

/**
 * <p>
 * 字段填充
 * </p>
 *
 * @author  Carry xie
 * @since 2017-06-26
 */
public class TableFill {

    /* 字段名称 */
    private String fieldName;
    /* 忽略类型 */
    private FieldFill ignore;

    private TableFill() {
        // to do nothing
    }

    public TableFill(String fieldName, FieldFill ignore) {
        this.fieldName = fieldName;
        this.ignore = ignore;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public FieldFill getIgnore() {
        return ignore;
    }

    public void setIgnore(FieldFill ignore) {
        this.ignore = ignore;
    }
}
