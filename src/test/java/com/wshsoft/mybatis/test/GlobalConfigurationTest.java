package com.wshsoft.mybatis.test;

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;

import com.wshsoft.mybatis.mapper.Condition;
import com.wshsoft.mybatis.mapper.Wrapper;
import com.wshsoft.mybatis.test.mysql.entity.NotPK;
import com.wshsoft.mybatis.test.mysql.entity.Test;
import com.wshsoft.mybatis.test.mysql.mapper.NotPKMapper;
import com.wshsoft.mybatis.test.mysql.mapper.TestMapper;

/**
 * <p>
 * 全局配置测试
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-12-22
 */
public class GlobalConfigurationTest extends CrudTest {

    /**
     * 全局配置测试
     */
    @org.junit.Test
    public void testGlobalConfig() {
        SqlSession session = this.sqlSessionFactory().openSession(false);
        TestMapper testMapper = session.getMapper(TestMapper.class);
        /*Wrapper type = Condition.instance().eq("id",1).or().in("type", new Object[]{1, 2, 3, 4, 5, 6});
        List list = testMapper.selectList(type);
        System.out.println(list.toString());*/
        Test test = new Test();
        test.setCreateTime(new Date());
        // 开启全局校验字符串会忽略空字符串
        test.setType("");
        testMapper.insert(test);

        SqlSession sqlSession = this.sqlSessionFactory().openSession(false);
        NotPKMapper pkMapper = sqlSession.getMapper(NotPKMapper.class);
        NotPK notPK = new NotPK();
        notPK.setUuid(UUID.randomUUID().toString());
        int num = pkMapper.insert(notPK);
        Assert.assertTrue(num > 0);
        NotPK notPK1 = pkMapper.selectOne(notPK);
        Assert.assertNotNull(notPK1);
        Wrapper type = Condition.create().eq("type", 12121212);
        Assert.assertFalse(type.isEmptyOfWhere());
        System.out.println(type.getSqlSegment());
        Assert.assertFalse(type.isEmptyOfWhere());
        pkMapper.selectPage(RowBounds.DEFAULT, type);
        NotPK notPK2 = null;
        try {
            notPK2 = pkMapper.selectById("1");
        } catch (Exception e) {
            System.out.println("因为没有主键,所以没有注入该方法");
        }
        Assert.assertNull(notPK2);
        int count = pkMapper.selectCount(Condition.EMPTY);
        pkMapper.selectList(Condition.create().orderBy("uuid"));
        pkMapper.selectList(Condition.create().eq("uuid", "uuid").orderBy("uuid"));
        Assert.assertTrue(count > 0);
        int deleteCount = pkMapper.delete(null);
        Assert.assertTrue(deleteCount > 0);
        session.rollback();
        sqlSession.commit();
    }

}
