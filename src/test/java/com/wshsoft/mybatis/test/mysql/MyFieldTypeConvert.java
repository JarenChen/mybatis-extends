package com.wshsoft.mybatis.test.mysql;

import com.wshsoft.mybatis.generator.config.converts.MySqlTypeConvert;
import com.wshsoft.mybatis.generator.config.rules.DbColumnType;

/**
 * <p>
 * 测试字段类型转换
 * </p>
 * 
 * @author Carry xie
 * @date 2017-01-20
 */
public class MyFieldTypeConvert extends MySqlTypeConvert {

	@Override
	public DbColumnType processTypeConvert(String fieldType) {
		System.out.println("转换类型：" + fieldType);
		return super.processTypeConvert(fieldType);
	}

}
