package com.wshsoft.mybatis.mapper;

import org.apache.ibatis.reflection.MetaObject;

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
     * @param metaObject 元对象
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
     * Common method to set value for java bean.
     *
     * @param fieldName java bean property name
     * @param fieldVal  java bean property value
     * @param metaObject meta object parameter
     */
    public MetaObjectHandler setFieldValByName(String fieldName, Object fieldVal, MetaObject metaObject){
        String[] fieldNames = metaObject.getGetterNames();
        boolean containsEt = false;
        for(String name:fieldNames){
            if(META_OBJ_PREFIX.equals(name)){
                containsEt = true;
                break;
            }
        }
        if(containsEt) {
            metaObject.setValue(META_OBJ_PREFIX +"."+ fieldName, fieldVal);
        }else{
            metaObject.setValue(fieldName, fieldVal);
        }
        return this;
    }

    /**
     * get value from java bean by propertyName
     * @param fieldName java bean property name
     * @param metaObject parameter wrapper
     * @return
     */
    public Object getFieldValByName(String fieldName, MetaObject metaObject){
        String[] fieldNames = metaObject.getGetterNames();
        boolean containsEt = false;
        for(String name:fieldNames){
            if(META_OBJ_PREFIX.equals(name)){
                containsEt = true;
                break;
            }
        }
        if(containsEt) {
            return metaObject.getValue(META_OBJ_PREFIX +"."+ fieldName);
        }else{
            return metaObject.getValue(fieldName);
        }
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
