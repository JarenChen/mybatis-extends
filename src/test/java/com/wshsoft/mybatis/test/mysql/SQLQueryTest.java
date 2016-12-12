package com.wshsoft.mybatis.test.mysql;

import com.wshsoft.mybatis.MybatisSessionFactoryBuilder;
import com.wshsoft.mybatis.mapper.SqlQuery;
import com.wshsoft.mybatis.plugins.pagination.Pagination;
import com.wshsoft.mybatis.toolkit.CollectionUtils;
import com.wshsoft.mybatis.toolkit.TableInfoHelper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 测试SQLQuery
 * </p>
 *
 * @author Carry xie
 * @date 2016-12-11
 */
public class SQLQueryTest {

	@Test
	public void test1() {
		/*
		 * 加载配置文件
		 */
		InputStream in = SQLQueryTest.class.getClassLoader().getResourceAsStream("mysql-config.xml");
		MybatisSessionFactoryBuilder mf = new MybatisSessionFactoryBuilder();
		SqlSessionFactory sessionFactory = mf.build(in);
		TableInfoHelper.initSqlSessionFactory(sessionFactory);

		boolean b = SqlQuery.db().insert("INSERT INTO `test` (`id`, `type`) VALUES ('107880983085826048', 't1021')");
		System.out.println(b);
		Assert.assertTrue(b);
		boolean b1 = SqlQuery.db().update("UPDATE `test` SET `type`='tttttttt' WHERE (`id`=107880983085826048)");
		System.out.println(b1);

		Assert.assertTrue(b1);
		List<Map<String, Object>> maps = SqlQuery.db().selectList("select * from test WHERE (`id`=107880983085826048)");
		System.out.println(maps);
		String type = (String) maps.get(0).get("type");
		System.out.println(type);
		Assert.assertEquals("tttttttt", type);
		boolean b2 = SqlQuery.db().delete("DELETE from test WHERE (`id`=107880983085826048)");
		System.out.println(b2);
		Assert.assertTrue(b2);
		List<Map<String, Object>> maps1 = SqlQuery.db()
				.selectList("select * from test WHERE (`id`=107880983085826048)");
		System.out.println(maps1);
		if (CollectionUtils.isEmpty(maps1)) {
			maps1 = null;
		}
		Assert.assertNull(maps1);
		List<Map<String, Object>> mapPage = SqlQuery.db().selectPage(new Pagination(1, 5), "select * from test ");
		System.out.println(mapPage);
		int i = SqlQuery.db().selectCount("select count(0) from test ");
		System.out.println("count:" + i);

	}

}
