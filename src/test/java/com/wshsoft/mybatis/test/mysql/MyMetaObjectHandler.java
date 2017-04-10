package com.wshsoft.mybatis.test.mysql;

import org.apache.ibatis.reflection.MetaObject;

import com.wshsoft.mybatis.mapper.IMetaObjectHandler;

/**
 * <p>
 * 测试，自定义元对象字段填充控制器，实现公共字段自动写入
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-08-28
 */
public class MyMetaObjectHandler implements IMetaObjectHandler {

	/**
	 * 测试 user 表 name 字段为空自动填充
	 */
	@Override
	public void insertFill(MetaObject metaObject) {
		// Object name = metaObject.getValue("name");
		// if (null == name) {
		// metaObject.setValue("name", "instert-fill");
		// }

		// 测试下划线
		Object testType = metaObject.getValue("testType");
		System.err.println("testType==" + testType);
		if (null == testType) {
			metaObject.setValue("testType", 3);
		}
	}

}
