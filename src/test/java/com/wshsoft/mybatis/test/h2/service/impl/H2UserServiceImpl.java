package com.wshsoft.mybatis.test.h2.service.impl;

import org.springframework.stereotype.Service;

import com.wshsoft.mybatis.service.impl.ServiceImpl;
import com.wshsoft.mybatis.test.h2.entity.mapper.H2UserMapper;
import com.wshsoft.mybatis.test.h2.entity.persistent.H2User;
import com.wshsoft.mybatis.test.h2.service.IH2UserService;

/**
 * <p>
 * Service层测试
 * </p>
 *
 * @author Carry xie
 * @date 2017-01-30
 */
@Service
public class H2UserServiceImpl extends ServiceImpl<H2UserMapper, H2User> implements IH2UserService {

}