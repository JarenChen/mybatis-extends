package com.wshsoft.mybatis.toolkit;

import com.wshsoft.mybatis.enums.IEnum;

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
	 * @param enumClass
	 *            枚举类
	 * @param value
	 *            枚举值
	 * @param <E>
	 *            对应枚举
	 * @return
	 */
	public static <E extends Enum<?> & IEnum> E valueOf(Class<E> enumClass, Object value) {
		E[] es = enumClass.getEnumConstants();
		for (E e : es) {
            if ((value instanceof String && e.getValue().equals(value))
                    || e.getValue() == value) {
				return e;
			}
		}
		return null;
	}

}
