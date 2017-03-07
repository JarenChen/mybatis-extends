package com.wshsoft.mybatis.test.proxy.mapper;

/**
 * <p>
 * 模拟 mybatis 加载 xml 执行 sql 返回
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-07-06
 */
public class UserMapperImpl implements IUserMapper {

    @Override
    public User selectById(Long id) {
        System.err.println(" ---  执行SQL 绑定数据 ---");
        User user = new User();
        user.setId(id);
        user.setName("mybatis-extends");
        user.setAge(100);
        return user;
    }

}
