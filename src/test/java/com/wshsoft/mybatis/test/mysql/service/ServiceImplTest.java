package com.wshsoft.mybatis.test.mysql.service;

import com.wshsoft.mybatis.test.mysql.entity.User;
import com.wshsoft.mybatis.test.mysql.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Service层测试
 * </p>
 * 
 * @author Carry xie
 * @date 2017-01-30
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-servlet.xml" })
public class ServiceImplTest {
	@Autowired
	private IUserService userService;

	@Test
	public void testInsertBatch() throws IOException {
		List<User> userList = new ArrayList<User>();
		for (int i = 0; i < 10; i++) {
			userList.add(new User("u-" + i, i, i));
		}
		boolean batchResult = userService.insertBatch(userList);
		System.err.println("batchResult: " + batchResult);
	}
}
