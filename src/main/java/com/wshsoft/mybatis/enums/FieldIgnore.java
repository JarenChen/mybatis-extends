package com.wshsoft.mybatis.enums;

/**
 * <p>
 * 字段忽略策略枚举类
 * </p>
 *
 * @author Carry xie
 * @Date 2016-09-09
 */
public enum FieldIgnore {
    DEFAULT(0, "默认方式"),
    INSERT(1, "忽略插入"),
    UPDATE(2, "忽略更新"),
    INSERT_UPDATE(3, "忽略插入和更新");

    /**
     * 主键
     */
    private final int key;

    /**
     * 描述
     */
    private final String desc;

    FieldIgnore(final int key, final String desc) {
        this.key = key;
        this.desc = desc;
    }

    public static FieldIgnore getIgnore(int key) {
        FieldIgnore[] fis = FieldIgnore.values();
        for (FieldIgnore fi : fis) {
            if (fi.getKey() == key) {
                return fi;
            }
        }
        return FieldIgnore.DEFAULT;
    }

    public int getKey() {
        return this.key;
    }

    public String getDesc() {
        return this.desc;
    }

}
