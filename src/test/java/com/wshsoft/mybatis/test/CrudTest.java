package com.wshsoft.mybatis.test;

import java.io.InputStream;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;

import com.wshsoft.mybatis.MybatisSessionFactoryBuilder;
import com.wshsoft.mybatis.entity.GlobalConfiguration;
import com.wshsoft.mybatis.toolkit.GlobalConfigUtils;

/**
 * <p>
 * CRUD 测试
 * </p>
 *
 * @author Carry xie
 * @date 2017-06-18
 */
public class CrudTest {

	public GlobalConfiguration globalConfiguration() {
		GlobalConfiguration global = GlobalConfigUtils.defaults();
		// global.setAutoSetDbType(true);
		// 设置全局校验机制为FieldStrategy.Empty
		global.setFieldStrategy(2);
		return global;
	}

	public SqlSessionFactory sqlSessionFactory() {
		return sqlSessionFactory("mysql-config.xml");
	}

	public SqlSessionFactory sqlSessionFactory(String configXml) {
		GlobalConfiguration global = this.globalConfiguration();
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/mybatis-extends?characterEncoding=UTF-8");
		dataSource.setUsername("root");
		dataSource.setPassword("root");
		dataSource.setMaxTotal(1000);
		GlobalConfigUtils.setMetaData(dataSource, global);
		// 加载配置文件
		InputStream inputStream = CrudTest.class.getClassLoader().getResourceAsStream("mysql-config.xml");
		MybatisSessionFactoryBuilder factoryBuilder = new MybatisSessionFactoryBuilder();
		factoryBuilder.setGlobalConfig(global);
		return factoryBuilder.build(inputStream);
	}

}
