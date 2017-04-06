package com.wshsoft.mybatis.enums;

/**
 * <p>
 * 字段策略枚举类
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-09-09
 */
public enum FieldStrategy {
    IGNORED(0, "ignored"), NOT_NULL(1, "not null"), NOT_EMPTY(2, "not empty");

    /** 主键 */
    private final int key;

    /** 描述 */
    private final String desc;

    FieldStrategy(final int key, final String desc) {
        this.key = key;
        this.desc = desc;
    }

    public static FieldStrategy getFieldStrategy(int key) {
        FieldStrategy[] fss = FieldStrategy.values();
        for (FieldStrategy fs : fss) {
            if (fs.getKey() == key) {
                return fs;
            }
        }
        return FieldStrategy.NOT_NULL;
    }

    public int getKey() {
        return this.key;
    }

    public String getDesc() {
        return this.desc;
    }

}
