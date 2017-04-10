package com.wshsoft.mybatis.test.plugins.paginationInterceptor.service.impl;

import org.springframework.stereotype.Service;

import com.wshsoft.mybatis.service.impl.ServiceImpl;
import com.wshsoft.mybatis.test.plugins.paginationInterceptor.entity.PageUser;
import com.wshsoft.mybatis.test.plugins.paginationInterceptor.mapper.PageUserMapper;
import com.wshsoft.mybatis.test.plugins.paginationInterceptor.service.PageUserService;

@Service
public class PageUserServiceImpl extends ServiceImpl<PageUserMapper, PageUser> implements PageUserService {

}
