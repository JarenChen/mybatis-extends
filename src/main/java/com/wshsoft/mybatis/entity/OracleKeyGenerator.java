package com.wshsoft.mybatis.entity;

import com.wshsoft.mybatis.mapper.IKeyGenerator;

/**
 * <p>
 * Oracle Key Sequence 生成器
 * </p>
 *
 * @author Carry xie
 * @Date 2017-05-08
 */
public class OracleKeyGenerator implements IKeyGenerator {

	@Override
	public String executeSql(TableInfo tableInfo) {
		return String.format("SELECT %s.NEXTVAL FROM DUAL", tableInfo.getKeySequence().value());
	}

}
