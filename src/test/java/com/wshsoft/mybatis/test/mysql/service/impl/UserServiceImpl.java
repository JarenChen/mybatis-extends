package com.wshsoft.mybatis.test.mysql.service.impl;

import org.springframework.stereotype.Service;

import com.wshsoft.mybatis.service.impl.ServiceImpl;
import com.wshsoft.mybatis.test.mysql.entity.User;
import com.wshsoft.mybatis.test.mysql.mapper.UserMapper;
import com.wshsoft.mybatis.test.mysql.service.IUserService;
import com.wshsoft.mybatis.toolkit.IdWorker;

/**
 * <p>
 * Service层测试
 * </p>
 * 
 * @author Carry xie
 * @date 2017-01-30
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

	// 注入测试
	@Override
	public void testSqlInjector() {
		Long id = IdWorker.getId();
		int rlt = baseMapper.insert(new User(id, "abc", 18, 1));
		System.err.println("插入ID：" + id + ", 执行结果：" + rlt);
		rlt = baseMapper.deleteLogicById(id);
		System.err.println("测试注入执行结果：" + rlt);
		System.err.println("逻辑修改：" + baseMapper.selectById(id).toString());
	}

}
