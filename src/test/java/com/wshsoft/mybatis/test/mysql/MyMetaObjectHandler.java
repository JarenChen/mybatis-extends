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
    public void insertFill(MetaObject metaObject) {
        // 测试下划线
        Object testType = getFieldValByName("testType", metaObject);
        System.out.println("testType=" + testType);
        if (testType == null) {// 如果不会设置这里不需要判断, 直接 set
            System.out.println("*************************");
            System.out.println("insert fill");
            System.out.println("*************************");
            setFieldValByName("testType", 3, metaObject);
        }
    }

    @Override
    public boolean openUpdateFill() {
        System.out.println("*************************");
        System.out.println(" 关闭更新填充 ");
        System.out.println("*************************");
        return false;
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 这里不会执行
    }
}
