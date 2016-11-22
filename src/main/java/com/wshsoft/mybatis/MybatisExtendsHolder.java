package com.wshsoft.mybatis;

import org.apache.ibatis.session.SqlSessionFactory;

/**
 * <p>
 * MybatisSqlSessionFactoryHolder
 * </p>
 *
 * @author Carry xie
 * @Date 2016-10-26
 */
public class MybatisExtendsHolder {

	private static SqlSessionFactory sqlSessionFactory;

	public static void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		MybatisExtendsHolder.sqlSessionFactory = sqlSessionFactory;
	}

	public static SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}
}
