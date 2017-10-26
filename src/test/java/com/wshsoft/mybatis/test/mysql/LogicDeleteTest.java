package com.wshsoft.mybatis.test.mysql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import com.wshsoft.mybatis.entity.GlobalConfiguration;
import com.wshsoft.mybatis.mapper.Condition;
import com.wshsoft.mybatis.test.CrudTest;
import com.wshsoft.mybatis.test.mysql.entity.User;
import com.wshsoft.mybatis.test.mysql.mapper.UserMapper;
import com.wshsoft.mybatis.toolkit.IdWorker;

/**
 * <p>
 * 逻辑删除测试
 * </p>
 *
 * @author Carry xie
 * @date 2017-04-09
 */
public class LogicDeleteTest extends CrudTest {

	@Override
	public GlobalConfiguration globalConfiguration() {
		GlobalConfiguration gc = super.globalConfiguration();
		gc.setLogicDeleteValue("-1");// 逻辑删除值 -1 测试字段 type
		return gc;
	}

	@Test
	public void test() {
		// 加载配置文件
		SqlSession session = this.sqlSessionFactory().openSession();
		UserMapper userMapper = session.getMapper(UserMapper.class);
		System.err.println(" debug run 查询执行 user 表数据变化！ ");
		long id = IdWorker.getId();
		int rlt = userMapper.insert(new User(id, "logic-delete-1", 18, 1));
		System.err.println("插入成功记录数：" + rlt);
		rlt = userMapper.deleteById(id);
		System.err.println("根据 ID 逻辑删除成功记录数：" + rlt);

		User uu = new User();
		uu.setId(333L);
		uu.setTestType(1);
		System.err.println("第一次：逻辑删除testType 改为 1 成功记录数：" + rlt);
		rlt = userMapper.insert(new User(IdWorker.getId(), "logic-delete-2", 28, 2));
		System.err.println("再插入一条成功记录数：" + rlt);
		rlt = userMapper.delete(Condition.create().eq("test_id", 1111));
		System.err.println("全表逻辑删除成功记录数：" + rlt);
		List<User> userList = userMapper.selectList(null);
		for (User u : userList) {
			System.out.println("全表逻辑删除 ( id= " + u.getId() + " ) 展示结果" + u.getTestType());
		}

		System.err.println("第二次：逻辑删除testType 改为 1 成功记录数：" + rlt);
		Map<String, Object> map = new HashMap<>();
		map.put("test_id", id);
		rlt = userMapper.deleteByMap(map);
		System.err.println("全表逻辑删除 ByMap 成功记录数：" + rlt);
		userList = userMapper.selectList(null);
		for (User u : userList) {
			System.out.println("全表逻辑删除 ( id= " + u.getId() + " ) 展示结果" + u.getTestType());
		}
	}
}
