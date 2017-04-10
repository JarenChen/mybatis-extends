package com.wshsoft.mybatis.test.mysql;

import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.wshsoft.mybatis.MybatisSessionFactoryBuilder;
import com.wshsoft.mybatis.test.mysql.entity.User;
import com.wshsoft.mybatis.test.mysql.mapper.UserMapper;
import com.wshsoft.mybatis.toolkit.IdWorker;

/**
 * <p>
 * MySQL 数据库，表引擎 MyISAM 不支持事务，请使用 InnoDB ！！！！
 * </p>
 * 
 * @author Carry xie
 * @date 2016-09-20
 */
public class TransactionalTest {

	/**
	 * <p>
	 * 事务测试
	 * </p>
	 */
	public static void main(String[] args) {
		/*
		 * 加载配置文件
		 */
		InputStream in = TransactionalTest.class.getClassLoader().getResourceAsStream("mysql-config.xml");
		MybatisSessionFactoryBuilder mf = new MybatisSessionFactoryBuilder();
		SqlSessionFactory sessionFactory = mf.build(in);
		SqlSession sqlSession = sessionFactory.openSession();
		/**
		 * 插入
		 */
		UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
		int rlt = userMapper.insert(new User(IdWorker.getId(), "1", 1, 1));
		System.err.println("--------- insertInjector --------- " + rlt);

		// session.commit();
		sqlSession.rollback();
		sqlSession.close();
	}

}
