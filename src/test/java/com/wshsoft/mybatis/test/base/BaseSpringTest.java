package com.wshsoft.mybatis.test.base;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wshsoft.mybatis.mapper.EntityWrapper;
import com.wshsoft.mybatis.plugins.Page;
import com.wshsoft.mybatis.plugins.pagination.Pagination;
import com.wshsoft.mybatis.test.mysql.entity.PhoneNumber;
import com.wshsoft.mybatis.test.mysql.entity.Role;
import com.wshsoft.mybatis.test.mysql.entity.User;
import com.wshsoft.mybatis.test.mysql.mapper.RoleMapper;
import com.wshsoft.mybatis.test.mysql.mapper.UserMapper;
import com.wshsoft.mybatis.toolkit.IdWorker;

/**
 * dao测试的基类
 * 
 * @author Carry xie
 * @Date 2014-5-16
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring.xml" })
public class BaseSpringTest {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RoleMapper roleMapper;

	@Before
	public void before() {

		Role role1 = new Role();
		role1.setId(1L);
		role1.setName("管理员");
		role1.setSort(1);
		role1.setDescription("管理员");
		int rlt1 = roleMapper.insert(role1);
		System.err.println("\n--------------insert----------------" + rlt1 + "\n\n");
		Role role2 = new Role();
		role2.setId(2L);
		role2.setName("普通人员");
		role2.setSort(2);
		role2.setDescription("基层人员");
		int rlt2 = roleMapper.insert(role2);
		System.err.println("\n--------------insertSelective----------------" + rlt2 + "\n\n");
		User userA = new User();
		userA.setId(1L);
		userA.setName("admin");
		userA.setAge(30);
		userA.setTestType(1);
		userA.setRole(role1);
		userA.setBirthday(new Date());
		userA.setPhone(new PhoneNumber("086", "0571", "88887777"));
		userMapper.insert(userA);

		List<User> ul = new ArrayList<User>();

		/* 手动输入 ID */
		ul.add(new User(IdWorker.getId(), "1", 1, 0, new Date()));
		ul.add(new User(12L, "2", 2, 1, new Date()));
		ul.add(new User(13L, "3", 3, 1, new Date()));
		ul.add(new User(14L, "delname", 4, 0, new Date()));
		ul.add(new User(15L, "5", 5, 1, new Date()));
		ul.add(new User(16L, "6", 6, 0, new Date()));
		ul.add(new User(17L, "7", 7, 0, new Date()));

		/* 使用 ID_WORKER 自动生成 ID */
		ul.add(new User("8", 8, 1, new Date()));
		ul.add(new User("9", 9, 1, new Date()));
		for (User user : ul) {
			int rlt3 = userMapper.insert(user);
			System.err.println("\n--------------insertBatch----------------" + rlt3 + "\n\n");
		}
		// int rlt3 = userMapper.insertBatch(ul);

	}

	@Test
	public void testSelectPage() {

		System.err.println("\n------------------list 分页查询 ---- 的所有数据--id--DESC--排序--------");
		Page<User> page = new Page<User>(1, 2);
		EntityWrapper<User> ew = new EntityWrapper<User>(new User(1));
		ew.setSqlSelect("age,name");
		ew.where("name like {0}", "'%dateBatch%'").and("age={0}", 3).orderBy("test_id", false);
		List<User> paginList = userMapper.selectPage(page, ew);
		page.setRecords(paginList);
		for (int i = 0; i < page.getRecords().size(); i++) {
			print(page.getRecords().get(i));
		}
		System.err.println(" 翻页：" + page.toString());
	}

	@Test
	public void testSelectListRow() {
		System.err.println("\n---------------xml---selectListRow 分页查询，不查询总数（此时可自定义 count 查询）----无查询条件--------------");
		// TODO 查询总数传 Page 对象即可
		List<User> rowList = userMapper.selectListRow(new Pagination(0, 2, false));
		for (int i = 0; i < rowList.size(); i++) {
			print(rowList.get(i));
		}
	}

	/*
	 * 打印测试信息
	 */
	private static void print(User user) {
		if (user != null) {
			System.out.println("\n user: id=" + user.getId() + ", name=" + user.getName() + ", age=" + user.getAge()
					+ ", testType=" + user.getTestType() + ", birthday=" + user.getBirthday());
		} else {
			System.out.println("\n user is null.");
		}
	}

	@After
	public void after() {
		roleMapper.deleteAll();
		userMapper.deleteAll();
	}
}
