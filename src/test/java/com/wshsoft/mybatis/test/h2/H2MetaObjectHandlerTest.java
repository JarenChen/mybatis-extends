package com.wshsoft.mybatis.test.h2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wshsoft.mybatis.test.h2.entity.mapper.H2UserMetaobjMapper;
import com.wshsoft.mybatis.test.h2.entity.persistent.H2UserMetaObj;

/**
 * <p>
 * TODO class
 * </p>
 *
 * @author Carry xie
 * @date 2017/5/31
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:h2/spring-test-h2-metaobj.xml" })
public class H2MetaObjectHandlerTest extends H2Test {

	@Autowired
	private H2UserMetaobjMapper userMapper;

	@BeforeClass
	public static void initDB() throws SQLException, IOException {
		@SuppressWarnings("resource")
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:h2/spring-test-h2-metaobj.xml");
		DataSource ds = (DataSource) context.getBean("dataSource");
		try (Connection conn = ds.getConnection()) {
			String createTableSql = readFile("user.ddl.sql");
			Statement stmt = conn.createStatement();
			stmt.execute(createTableSql);
			stmt.execute("truncate table h2user");
			executeSql(stmt, "user.insert.sql");
			conn.commit();
		}
	}

    @Test
    public void testMetaObjectHandler() {
        H2UserMetaObj user = new H2UserMetaObj();
        user.setName("metaobjtest");
        user.setVersion(1);
        user.setAge(12);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        user.setLastUpdatedDt(new Timestamp(cal.getTimeInMillis()));
        user.setDesc("abc");
        userMapper.insert(user);
        System.out.println("before update: getLastUpdatedDt=" + user.getLastUpdatedDt());
        Assert.assertNotNull(userMapper.selectById(user.getId()).getTestType());

		user.setName("999");
		userMapper.updateById(user);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
		Long id = user.getId();
		H2UserMetaObj userDB = userMapper.selectById(id);

		// MyMetaObjectHandler.insertFill() : set default testType value=3
		Assert.assertEquals(3, userDB.getTestType().intValue());
		Assert.assertEquals("999", userDB.getName());

        Date lastUpdatedDt = userDB.getLastUpdatedDt();
        System.out.println("after update: testDate=" + lastUpdatedDt);
        String versionDateStr = sdf.format(lastUpdatedDt);
        //MyMetaObjectHandler.updateFill() : set lastUpdatedDt=currentTimestamp
        Assert.assertEquals(sdf.format(new Date()), versionDateStr);//before update: lastUpdatedDt=currentTimestamp-1day
    }

    @Test
    public void testMetaObjectHandlerNullInsert4Update() {
        H2UserMetaObj user = new H2UserMetaObj();
        user.setName("metaobjtest");
        user.setVersion(1);
        user.setAge(12);
        user.setDesc("abc");
        userMapper.insert(user);
        System.out.println("before update: getLastUpdatedDt=" + user.getLastUpdatedDt());
        Assert.assertNotNull(userMapper.selectById(user.getId()).getTestType());
        user.setName("999");
        userMapper.updateById(user);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        Long id = user.getId();
        H2UserMetaObj userDB = userMapper.selectById(id);
        Date lastUpdatedDt = userDB.getLastUpdatedDt();
        System.out.println("after update: testDate=" + lastUpdatedDt);
        String versionDateStr = sdf.format(lastUpdatedDt);
        //MyMetaObjectHandler.updateFill() : set lastUpdatedDt=currentTimestamp
        Assert.assertEquals(sdf.format(new Date()), versionDateStr);//before update: lastUpdatedDt=currentTimestamp-1day
    }


	@Test
	public void testInsertMy() {
		String name = "testInsertMy";
		int version = 1;
		int row = userMapper.myInsertWithNameVersion(name, version);
		Assert.assertEquals(1, row);
	}

	@Test
	public void testUpdateMy() {
		H2UserMetaObj user = new H2UserMetaObj();
		user.setName("myUpdate");
		user.setVersion(1);
		userMapper.insert(user);
		Long id = user.getId();
		H2UserMetaObj dbUser = userMapper.selectById(id);
		Assert.assertNotNull(dbUser);
		Assert.assertEquals("myUpdate", dbUser.getName());

		Assert.assertEquals(1, userMapper.myUpdateWithNameId(id, "updateMy"));

		dbUser = userMapper.selectById(id);
		Assert.assertNotNull(dbUser);
		Assert.assertEquals("updateMy", dbUser.getName());
		Assert.assertEquals(1, user.getVersion().intValue());
	}

}
