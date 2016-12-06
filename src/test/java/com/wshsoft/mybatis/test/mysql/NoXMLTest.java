package com.wshsoft.mybatis.test.mysql;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.wshsoft.mybatis.MybatisSessionFactoryBuilder;
import com.wshsoft.mybatis.test.mysql.entity.Test;
import com.wshsoft.mybatis.toolkit.IdWorker;

/**
 * <p>
 * 测试没有XML同样注入CRUD SQL
 * </p>
 *
 * @author Carry xie
 * @date 2016-09-26
 */
public class NoXMLTest {

	public static void main(String[] args) {
		/*
		 * 加载配置文件
		 */
		InputStream in = NoXMLTest.class.getClassLoader().getResourceAsStream("mysql-config.xml");
		MybatisSessionFactoryBuilder mf = new MybatisSessionFactoryBuilder();
		SqlSessionFactory sessionFactory = mf.build(in);
		SqlSession sqlSession = sessionFactory.openSession();
		/**
		 * 查询是否有结果
		 */
		TestMapper testMapper = sqlSession.getMapper(TestMapper.class);
		testMapper.insert(new Test(IdWorker.getId(), "Caratacus"));
		Map<String, Object> map = new HashMap();
		map.put("type", null);
		map.put("id", null);
		List<Test> tests = testMapper.selectByMap(map);
		if (null != tests) {
			for (Test test : tests) {
				System.out.println("id:"+test.getId()+ " , type:" + test.getType());
			}
		} else {
			System.err.println(" tests is null. ");
		}
		testMapper.delete(null);

	}

}
