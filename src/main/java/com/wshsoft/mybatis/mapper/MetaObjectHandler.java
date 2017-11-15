package com.wshsoft.mybatis.mapper;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

/**
 * <p>
 * 元对象字段填充控制器抽象类，实现公共字段自动写入
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-08-28
 */
public abstract class MetaObjectHandler {

	protected static final String META_OBJ_PREFIX = "et";

	/**
	 * <p>
	 * 插入元对象字段填充
	 * </p>
	 *
	 * @param metaObject
	 *            元对象
	 */
	public abstract void insertFill(MetaObject metaObject);

	/**
	 * 更新元对象字段填充（用于更新时对公共字段的填充）
	 * 
	 * @param metaObject
	 *            元对象
	 */
	public abstract void updateFill(MetaObject metaObject);

    /**
     * <p>
     * Common method to set value for java bean.
     * </p>
     * <p>
     * 如果包含前缀 et 使用该方法，否则可以直接 metaObject.setValue(fieldName, fieldVal);
     * </p>
     *
     * @param fieldName  java bean property name
     * @param fieldVal   java bean property value
     * @param metaObject meta object parameter
     */
    public MetaObjectHandler setFieldValByName(String fieldName, Object fieldVal, MetaObject metaObject) {
        if (metaObject.hasSetter(fieldName) && metaObject.hasGetter(fieldName)) {
            metaObject.setValue(fieldName, fieldVal);
        } else if (metaObject.hasGetter(META_OBJ_PREFIX)) {
            Object et = metaObject.getValue(META_OBJ_PREFIX);
            if (et != null) {
                MetaObject etMeta = SystemMetaObject.forObject(et);
                if (etMeta.hasSetter(fieldName)) {
                    etMeta.setValue(fieldName, fieldVal);
                }
            }
        }
        return this;
    }

	/**
	 * <p>
	 * get value from java bean by propertyName
	 * </p>
	 * <p>
	 * 如果包含前缀 et 使用该方法，否则可以直接 metaObject.setValue(fieldName, fieldVal);
	 * </p>
	 *
	 * @param fieldName
	 *            java bean property name
	 * @param metaObject
	 *            parameter wrapper
	 * @return
	 */
	public Object getFieldValByName(String fieldName, MetaObject metaObject) {
		if (metaObject.hasGetter(fieldName)) {
			return metaObject.getValue(fieldName);
		} else if (metaObject.hasGetter(META_OBJ_PREFIX + "." + fieldName)) {
			return metaObject.getValue(META_OBJ_PREFIX + "." + fieldName);
		}
		return null;
	}

	/**
	 * 开启插入填充
	 */
	public boolean openInsertFill() {
		return true;
	}

	/**
	 * 开启更新填充
	 */
	public boolean openUpdateFill() {
		return true;
	}

}
