package com.wshsoft.mybatis.test.h2.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wshsoft.mybatis.plugins.Page;
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

    @Autowired
    H2UserMapper userMapper;

    @Override
    public int myInsert(String name, int version) {
        return userMapper.myInsertWithNameVersion(name, version);
    }

    @Override
    public int myInsertWithParam(String name, int version) {
        H2User user = new H2User();
        user.setName(name);
        user.setVersion(version);
        return userMapper.myInsertWithParam(user);
    }

    @Override
    public int myInsertWithoutParam(String name, int version) {
        H2User user = new H2User();
        user.setName(name);
        user.setVersion(version);
        return userMapper.myInsertWithoutParam(user);
    }

    @Override
    public int myUpdate(Long id, String name) {
        return userMapper.myUpdateWithNameId(id, name);
    }

    @Override
    public List<H2User> queryWithParamInSelectStatememt(Map<String, Object> param) {
        return userMapper.selectUserWithParamInSelectStatememt(param);
    }

    @Override
    public Page<H2User> queryWithParamInSelectStatememt4Page(Map<String, Object> param, Page<H2User> page) {
        page.setSearchCount(true);
        userMapper.selectUserWithParamInSelectStatememt4Page(param, page);
        return page;
    }

    @Override
    public int selectCountWithParamInSelectItems(Map<String, Object> param) {
        return userMapper.selectCountWithParamInSelectItems(param);
    }
}
