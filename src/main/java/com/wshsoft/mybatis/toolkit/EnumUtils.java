package com.wshsoft.mybatis.toolkit;

import com.wshsoft.mybatis.enums.IEnum;

import java.math.BigDecimal;

/**
 * <p>
 * 枚举处理工具类
 * </p>
 *
 * @author Carry xie
 * @Date 2017-10-11
 */
public class EnumUtils {

    /**
     * <p>
     * 值映射为枚举
     * </p>
     *
     * @param enumClass 枚举类
     * @param value     枚举值
     * @param <E>       对应枚举
     * @return
     */
    public static <E extends Enum<?> & IEnum> E valueOf(Class<E> enumClass, Object value) {
        E[] es = enumClass.getEnumConstants();
        for (E e : es) {
            if (e.getValue() == value) {
                // 基本类型
                return e;
            } else if (value instanceof String && e.getValue().equals(value)) {
                // 字符串类型
                return e;
            } else if (value instanceof BigDecimal && ((BigDecimal) e.getValue()).compareTo((BigDecimal) value) == 0) {
                // 结果是:-1 小于,0 等于,1 大于
                return e;
            } else if (String.valueOf(e.getValue()).equals(String.valueOf(value))) {
                // 其他类型
                return e;
            }
        }
        return null;
    }

}
