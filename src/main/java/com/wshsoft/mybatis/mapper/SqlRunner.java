package com.wshsoft.mybatis.mapper;

import com.wshsoft.mybatis.entity.GlobalConfiguration;
import com.wshsoft.mybatis.entity.TableInfo;
import com.wshsoft.mybatis.plugins.Page;
import com.wshsoft.mybatis.toolkit.CollectionUtils;
import com.wshsoft.mybatis.toolkit.StringUtils;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * SqlRunner 执行 SQL
 * </p>
 *
 * @author Carry xie
 * @Date 2016-12-11
 */
public class SqlRunner {

	private static final Log logger = LogFactory.getLog(SqlRunner.class);
	// 默认FACTORY
	public static SqlSessionFactory FACTORY;
	public static final String INSERT = "com.wshsoft.mybatis.mapper.SqlRunner.Insert";
	public static final String DELETE = "com.wshsoft.mybatis.mapper.SqlRunner.Delete";
	public static final String UPDATE = "com.wshsoft.mybatis.mapper.SqlRunner.Update";
	public static final String SELECT = "com.wshsoft.mybatis.mapper.SqlRunner.Select";
	public static final String COUNT = "com.wshsoft.mybatis.mapper.SqlRunner.Count";
	public static final String SQLScript = "${sql}";
	public static final String SQL = "sql";

	// 单例Query
	public static final SqlRunner DEFAULT = new SqlRunner();
	private SqlSessionFactory sqlSessionFactory;

	public SqlRunner() {
		this.sqlSessionFactory = FACTORY;
	}

	public SqlRunner(Class<?> clazz) {
		TableInfo tableInfo = SqlHelper.table(clazz);
		GlobalConfiguration globalConfiguration = GlobalConfiguration.GlobalConfig(tableInfo.getConfigMark());
		this.sqlSessionFactory = globalConfiguration.getSqlSessionFactory();
	}

	public boolean insert(String sql, Object... args) {
		return SqlHelper.retBool(sqlSession().insert(INSERT, sqlMap(sql, args)));
	}

	public boolean delete(String sql, Object... args) {
		return SqlHelper.retBool(sqlSession().delete(DELETE, sqlMap(sql, args)));
	}

	/**
	 * 获取sqlMap参数
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	private Map<String, String> sqlMap(String sql, Object... args) {
		Map<String, String> sqlMap = new HashMap<String, String>();
		sqlMap.put(SQL, StringUtils.sqlArgsFill(sql, args));
		return sqlMap;
	}

	public boolean update(String sql, Object... args) {
		return SqlHelper.retBool(sqlSession().update(UPDATE, sqlMap(sql, args)));
	}

	public List<Map<String, Object>> selectList(String sql, Object... args) {
		return sqlSession().selectList(SELECT, sqlMap(sql, args));
	}

	public int selectCount(String sql, Object... args) {
		return SqlHelper.retCount(sqlSession().<Integer> selectOne(COUNT, sqlMap(sql, args)));
	}

	public Map<String, Object> selectOne(String sql, Object... args) {
		return SqlHelper.getObject(selectList(sql, args));
	}

	public Page<Map<String, Object>> selectPage(Page page, String sql, Object... args) {
		if (null == page) {
			return null;
		}
		page.setRecords(sqlSession().selectList(SELECT, sqlMap(sql, args), page));
		return page;
	}

	/**
	 * 获取默认的SqlQuery(适用于单库)
	 * 
	 * @return
	 */
	public static SqlRunner db() {
		// 初始化的静态变量 还是有前后加载的问题 该判断只会执行一次
		if (DEFAULT.sqlSessionFactory == null) {
			DEFAULT.sqlSessionFactory = FACTORY;
		}
		return DEFAULT;
	}

	/**
	 * 根据当前class对象获取SqlQuery(适用于多库)
	 * 
	 * @param clazz
	 * @return
	 */
	public static SqlRunner db(Class<?> clazz) {
		return new SqlRunner(clazz);
	}

	/**
	 * <p>
	 * 获取Session 默认自动提交
	 * <p/>
	 */
	private SqlSession sqlSession() {
		return sqlSessionFactory.openSession(true);
	}

}
