package com.wshsoft.mybatis.mapper;

import com.wshsoft.mybatis.toolkit.StringUtils;

/**
 * <p>
 * 条件查询构造器
 * </p>
 *
 * @author Carry xie
 * @date 2016-11-7
 */
@SuppressWarnings({ "rawtypes", "serial" })
public class Condition extends Wrapper {

	/**
	 * 构建一个Empty条件构造 避免传递参数使用null
	 */
	public static Condition Empty() {
		return Condition.instance();
	}

	/**
	 * 获取实例
	 */
	public static Condition instance() {
		return new Condition();
	}

	/**
	 * SQL 片段
	 */
	@Override
	public String getSqlSegment() {
		/*
		 * 无条件
		 */
		String sqlWhere = sql.toString();
		if (StringUtils.isEmpty(sqlWhere)) {
			return null;
		}

		return sqlWhere;
	}

}
