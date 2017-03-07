package com.wshsoft.mybatis.test.mysql.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.wshsoft.mybatis.mapper.BaseMapper;
import com.wshsoft.mybatis.test.mysql.entity.User;

/**
 * <p>
 * 继承 BaseMapper，就自动拥有CRUD方法
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-01-23
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 用户列表，分页显示
     * 
     * @param pagination 传递参数包含该属性，即自动分页
     * @return
     */
    List<User> selectListRow(RowBounds pagination);

    /**
     * 注解插入【测试】
     */
    @Insert("insert into user(test_id,name,age) values(#{id},#{name},#{age})")
    int insertInjector(User user);

    /**
     * 自定义注入方法
     */
    int deleteAll();

    /**
     * 根据主键批量查询
     * 
     * @param pagination
     * @param ids
     * @return
     */
    List<User> forSelect(RowBounds pagination, @Param("ids") List<String> ids);
}
