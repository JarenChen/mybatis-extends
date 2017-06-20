package com.wshsoft.mybatis.test.mysql;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.wshsoft.mybatis.plugins.Page;
import com.wshsoft.mybatis.test.CrudTest;
import com.wshsoft.mybatis.test.mysql.entity.Test;
import com.wshsoft.mybatis.test.mysql.entity.User;
import com.wshsoft.mybatis.test.mysql.mapper.TestMapper;
import com.wshsoft.mybatis.test.mysql.mapper.UserMapper;

/**
 * <p>
 * 循环标签 查询分页失效 测试类
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-12-21
 */
public class CircularLabelsTest extends CrudTest {

    /**
     * 循环标签 测试
     */
    @org.junit.Test
    public void test() {
        SqlSession session = this.sqlSessionFactory().openSession();
        UserMapper userMapper = session.getMapper(UserMapper.class);
        Page<User> page = new Page<>(1, 6);
        List<User> users = userMapper.forSelect(page, Arrays.asList("1", "2", "3"));
        System.out.println(users.toString());
        System.out.println(page);
        User user = new User();
        user.setId(1L);
        User users1 = userMapper.selectOne(user);
        System.out.println(users1);
        TestMapper mapper = session.getMapper(TestMapper.class);
        Test test = new Test();
        test.setCreateTime(new Date());
        test.setType("11111");
        mapper.insert(test);
        session.rollback();
    }
}
