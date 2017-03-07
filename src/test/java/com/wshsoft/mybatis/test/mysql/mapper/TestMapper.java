package com.wshsoft.mybatis.test.mysql.mapper;

import org.apache.ibatis.annotations.Insert;

import com.wshsoft.mybatis.mapper.BaseMapper;
import com.wshsoft.mybatis.test.mysql.entity.Test;

/**
 * <p>
 * 继承 BaseMapper，就自动拥有CRUD方法
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-09-25
 */
public interface TestMapper extends BaseMapper<Test> {

    /**
     * 注解插入【测试】
     */
    @Insert("insert into test(id,type) values(#{id},#{type})")
    int insertInjector(Test test);

}
