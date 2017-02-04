package com.wshsoft.mybatis.test.mysql.service.impl;

import org.springframework.stereotype.Service;

import com.wshsoft.mybatis.service.impl.ServiceImpl;
import com.wshsoft.mybatis.test.mysql.entity.User;
import com.wshsoft.mybatis.test.mysql.mapper.UserMapper;
import com.wshsoft.mybatis.test.mysql.service.IUserService;

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

}
