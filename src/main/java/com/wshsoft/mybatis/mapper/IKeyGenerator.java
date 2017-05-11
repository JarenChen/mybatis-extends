package com.wshsoft.mybatis.mapper;

import com.wshsoft.mybatis.entity.TableInfo;

/**
 * <p>
 * 表关键词 key 生成器接口
 * </p>
 *
 * @author Carry xie
 * @Date 2017-05-08
 */
public interface IKeyGenerator {

	/**
	 * <p>
	 * 执行 key 生成 SQL
	 * </p>
	 *
	 * @param tableInfo
	 *            数据库表反射信息
	 * @return
	 */
	String executeSql(TableInfo tableInfo);

}
