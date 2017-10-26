package com.wshsoft.mybatis.test.oracle;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wshsoft.mybatis.mapper.EntityWrapper;
import com.wshsoft.mybatis.test.oracle.config.DruidOracleDBConfig;
import com.wshsoft.mybatis.test.oracle.config.OracleMybatisExtendsConfig;
import com.wshsoft.mybatis.test.oracle.entity.TestSequser;
import com.wshsoft.mybatis.test.oracle.mapper.TestSequserMapper;

/**
 * <p>
 * </p>
 *
 * @author Carry xie
 * @date 2017/7/4
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { DruidOracleDBConfig.class, OracleMybatisExtendsConfig.class })
public class OracleDruidTest {

	@Autowired
	TestSequserMapper sequserMapper;

	@Test
	public void testSelectListMp() {
		List<TestSequser> list = sequserMapper.selectList(new EntityWrapper<TestSequser>());
		for (TestSequser u : list) {
			System.out.println(u);
		}
	}

	@Test
	public void testSelectListNative() {
		List<TestSequser> list = sequserMapper.getList();
		for (TestSequser u : list) {
			System.out.println(u);
		}
	}

	@Test
	public void testInsert() {
		TestSequser user = new TestSequser();
		user.setName("seqtest0627");
		user.setAge(12);
		user.setTestType(1);
		sequserMapper.insert(user);
		Long id = user.getId();
		Assert.assertNotNull(id);

	}

}
