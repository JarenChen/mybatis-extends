package com.wshsoft.mybatis.test;

import com.wshsoft.mybatis.MybatisSessionFactoryBuilder;
import com.wshsoft.mybatis.entity.GlobalConfiguration;
import com.wshsoft.mybatis.mapper.Condition;
import com.wshsoft.mybatis.test.mysql.mapper.NotPKMapper;
import com.wshsoft.mybatis.test.mysql.mapper.TestMapper;
import com.wshsoft.mybatis.test.mysql.entity.NotPK;
import com.wshsoft.mybatis.test.mysql.entity.Test;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Assert;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 全局配置测试
 * </p>
 *
 * @author Carry xie
 * @Date 2016-12-22
 */
public class GlobalConfigurationTest {

	/**
	 * 全局配置测试
	 */
	public static void main(String[] args) {
        GlobalConfiguration global = GlobalConfiguration.defaults();
        global.setAutoSetDbType(true);
        // 设置全局校验机制为FieldStrategy.Empty
        global.setFieldStrategy(2);
        BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/mybatis-extends?characterEncoding=UTF-8");
		dataSource.setUsername("root");
		dataSource.setPassword("521");
		dataSource.setMaxTotal(1000);
		GlobalConfiguration.setMetaData(dataSource,global);
		// 加载配置文件
		InputStream inputStream = GlobalConfigurationTest.class.getClassLoader().getResourceAsStream("mysql-config.xml");
		MybatisSessionFactoryBuilder factoryBuilder = new MybatisSessionFactoryBuilder();
		factoryBuilder.setGlobalConfig(global);
		SqlSessionFactory sessionFactory = factoryBuilder.build(inputStream);
		SqlSession session = sessionFactory.openSession(false);
		TestMapper testMapper = session.getMapper(TestMapper.class);
        List<Map<String, Object>> list = testMapper.selectMaps(null);
        Test test = new Test();
		test.setCreateTime(new Date());
		// 开启全局校验字符串会忽略空字符串
		test.setType("");
		testMapper.insert(test);

		SqlSession sqlSession = sessionFactory.openSession(false);
		NotPKMapper pkMapper = sqlSession.getMapper(NotPKMapper.class);
		NotPK notPK = new NotPK();
		notPK.setUuid(UUID.randomUUID().toString());
		int num = pkMapper.insert(notPK);
		Assert.assertTrue(num > 0);
		NotPK notPK1 = pkMapper.selectOne(notPK);
		Assert.assertNotNull(notPK1);
		pkMapper.selectPage(RowBounds.DEFAULT, Condition.Empty());
		NotPK notPK2 = null;
		try {
			notPK2 = pkMapper.selectById("1");
		} catch (Exception e) {
			System.out.println("因为没有主键,所以没有注入该方法");
		}
		Assert.assertNull(notPK2);
		int count = pkMapper.selectCount(Condition.Empty());
		Assert.assertTrue(count > 0);
		int deleteCount = pkMapper.delete(null);
		Assert.assertTrue(deleteCount > 0);
		session.rollback();
		sqlSession.commit();
	}
}
