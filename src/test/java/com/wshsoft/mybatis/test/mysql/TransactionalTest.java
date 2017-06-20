package com.wshsoft.mybatis.test.mysql;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import com.wshsoft.mybatis.test.CrudTest;
import com.wshsoft.mybatis.test.mysql.entity.User;
import com.wshsoft.mybatis.test.mysql.mapper.UserMapper;
import com.wshsoft.mybatis.toolkit.IdWorker;

/**
 * <p>
 * MySQL 数据库，表引擎 MyISAM 不支持事务，请使用 InnoDB ！！！！
 * </p>
 * 
 * @author Carry xie
 * @date 2016-09-20
 */
public class TransactionalTest extends CrudTest {

    /**
     * <p>
     * 事务测试
     * </p>
     */
    @Test
    public void test() {
        SqlSession sqlSession = this.sqlSessionFactory().openSession();

        /**
         * 插入
         */
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        int rlt = userMapper.insert(new User(IdWorker.getId(), "1", 1, 1));
        System.err.println("--------- insertInjector --------- " + rlt);

		// session.commit();
		sqlSession.rollback();
		sqlSession.close();
	}

}
