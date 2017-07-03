package com.wshsoft.mybatis.test;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.wshsoft.mybatis.plugins.pagination.Pagination;
import com.wshsoft.mybatis.spring.MybatisMapperRefresh;
import com.wshsoft.mybatis.test.mysql.mapper.UserMapper;
import com.wshsoft.mybatis.toolkit.SystemClock;

/**
 * <p>
 * 切莫用于生产环境（后果自负）<br>
 * Mybatis 映射文件热加载（发生变动后自动重新加载）.<br>
 * 方便开发时使用，不用每次修改xml文件后都要去重启应用.<br>
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-08-25
 */
public class MybatisMapperRefreshTest extends CrudTest {


    /**
     * 测试 Mybatis XML 修改自动刷新
     */
    public static void main(String[] args) throws Exception {
        Resource[] resource = new ClassPathResource[]{new ClassPathResource("mysql/UserMapper.xml")};
        SqlSessionFactory sessionFactory = new CrudTest().sqlSessionFactory();
        new MybatisMapperRefresh(sessionFactory, 0, 5, true);
		boolean isReturn = false;
		SqlSession session = null;
		while (!isReturn) {
			try {
				session = sessionFactory.openSession();
				UserMapper userMapper = session.getMapper(UserMapper.class);
				userMapper.selectListRow(new Pagination(1, 10));
				resource[0].getFile().setLastModified(SystemClock.now());
				session.commit();
				session.close();
				Thread.sleep(5000);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (session != null) {
					session.close();
				}
				Thread.sleep(5000);
			}
		}
		System.exit(0);
	}
}
