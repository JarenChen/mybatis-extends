package com.wshsoft.mybatis.test.mysql.service;

import com.wshsoft.mybatis.service.IService;
import com.wshsoft.mybatis.test.mysql.entity.User;

/**
 * <p>
 * Service层测试
 * </p>
 * 
 * @author Carry xie
 * @date 2017-01-30
 */
public interface IUserService extends IService<User> {

	void testSqlInjector();

}
