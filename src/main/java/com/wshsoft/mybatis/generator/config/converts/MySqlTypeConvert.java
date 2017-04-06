package com.wshsoft.mybatis.generator.config.converts;

import com.wshsoft.mybatis.generator.config.ITypeConvert;
import com.wshsoft.mybatis.generator.config.rules.DbColumnType;

/**
 * <p>
 * MYSQL 数据库字段类型转换
 * </p>
 * 
 * @author Carry xie
 * @date 2017-01-20
 */
public class MySqlTypeConvert implements ITypeConvert {

    public DbColumnType processTypeConvert(String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char") || t.contains("text")) {
            return DbColumnType.STRING;
        } else if (t.contains("bigint")) {
            return DbColumnType.LONG;
        } else if (t.contains("int")) {
            return DbColumnType.INTEGER;
        } else if (t.contains("date") || t.contains("time") || t.contains("year")) {
            return DbColumnType.DATE;
        } else if (t.contains("text")) {
            return DbColumnType.STRING;
        } else if (t.contains("bit")) {
            return DbColumnType.BOOLEAN;
        } else if (t.contains("decimal")) {
            return DbColumnType.BIG_DECIMAL;
        } else if (t.contains("blob")) {
            return DbColumnType.BYTE_ARRAY;
        } else if (t.contains("float")) {
            return DbColumnType.FLOAT;
        } else if (t.contains("double")) {
            return DbColumnType.DOUBLE;
        } else if (t.contains("json") || t.contains("enum")) {
            return DbColumnType.STRING;
        }
        return DbColumnType.STRING;
    }

}
