package com.wshsoft.mybatis.test.mysql;

import org.apache.ibatis.reflection.MetaObject;

import com.wshsoft.mybatis.mapper.MetaObjectHandler;

/**
 * <p>
 * 测试，自定义元对象字段填充控制器，实现公共字段自动写入
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-08-28
 */
public class MyMetaObjectHandler extends MetaObjectHandler {

	/**
	 * 测试 user 表 name 字段为空自动填充
	 */
	@Override
	public void insertFill(MetaObject metaObject) {
		// Object name = metaObject.getValue("name");
		// if (null == name) {
		// metaObject.setValue("name", "instert-fill");
		// }

        System.out.println("*************************");
        System.out.println("insert fill");
        System.out.println("*************************");

        // 测试下划线
        Object testType = getFieldValByName("testType", metaObject);
        System.out.println("testType="+testType);
        if(testType==null){
            setFieldValByName("testType", 3, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        System.out.println("*************************");
        System.out.println("update fill");
        System.out.println("*************************");
        setFieldValByName("lastUpdatedDt", new Timestamp(System.currentTimeMillis()), metaObject);
    }
}
