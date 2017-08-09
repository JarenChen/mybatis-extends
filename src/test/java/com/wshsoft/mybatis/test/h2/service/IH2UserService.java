package com.wshsoft.mybatis.test.h2.service;

import java.util.List;
import java.util.Map;
import com.wshsoft.mybatis.plugins.Page;
import com.wshsoft.mybatis.service.IService;
import com.wshsoft.mybatis.test.h2.entity.persistent.H2User;

/**
 * <p>
 * Service层测试
 * </p>
 *
 * @author Carry xie
 * @date 2017-01-30
 */
public interface IH2UserService extends IService<H2User> {

    int myInsert(String name, int version);

    int myInsertWithParam(String name, int version);

    int myInsertWithoutParam(String name, int version);

    int myUpdate(Long id, String name);

    List<H2User> queryWithParamInSelectStatememt(Map<String,Object> param);

    Page<H2User> queryWithParamInSelectStatememt4Page(Map<String,Object> param, Page<H2User> page);

    int selectCountWithParamInSelectItems(Map<String,Object> param);
}
