package com.wshsoft.mybatis.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.transaction.annotation.Transactional;

import com.wshsoft.mybatis.plugins.Page;
import com.wshsoft.mybatis.toolkit.GlobalConfigUtils;
import com.wshsoft.mybatis.toolkit.StringUtils;

/**
 * <p>
 * SqlRunner 执行 SQL
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-12-11
 */
public class SqlRunner {

	public static final String INSERT = "com.wshsoft.mybatis.mapper.SqlRunner.Insert";
	public static final String DELETE = "com.wshsoft.mybatis.mapper.SqlRunner.Delete";
	public static final String UPDATE = "com.wshsoft.mybatis.mapper.SqlRunner.Update";
	public static final String SELECT_LIST = "com.wshsoft.mybatis.mapper.SqlRunner.SelectList";
	public static final String SELECT_OBJS = "com.wshsoft.mybatis.mapper.SqlRunner.SelectObjs";
	public static final String COUNT = "com.wshsoft.mybatis.mapper.SqlRunner.Count";
	public static final String SQLScript = "${sql}";
	public static final String SQL = "sql";

	// 单例Query
	public static final SqlRunner DEFAULT = new SqlRunner();
	// 默认FACTORY
	public static SqlSessionFactory FACTORY;
	private SqlSessionFactory sqlSessionFactory;

	private Class<?> clazz;

	public SqlRunner() {
		this.sqlSessionFactory = FACTORY;
	}

	public SqlRunner(Class<?> clazz) {
		this.clazz = clazz;
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

	@Transactional
	public boolean insert(String sql, Object... args) {
		return SqlHelper.retBool(sqlSession().insert(INSERT, sqlMap(sql, args)));
	}

	@Transactional
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
		Map<String, String> sqlMap = new HashMap<>();
		sqlMap.put(SQL, StringUtils.sqlArgsFill(sql, args));
		return sqlMap;
	}

	@Transactional
	public boolean update(String sql, Object... args) {
		return SqlHelper.retBool(sqlSession().update(UPDATE, sqlMap(sql, args)));
	}

	public List<Map<String, Object>> selectList(String sql, Object... args) {
		return sqlSession().selectList(SELECT_LIST, sqlMap(sql, args));
	}

	public List<Object> selectObjs(String sql, Object... args) {
		return sqlSession().selectList(SELECT_OBJS, sqlMap(sql, args));
	}

	public Object selectObj(String sql, Object... args) {
		return SqlHelper.getObject(selectObjs(sql, args));
	}

	public int selectCount(String sql, Object... args) {
		return SqlHelper.retCount(sqlSession().<Integer> selectOne(COUNT, sqlMap(sql, args)));
	}

	public Map<String, Object> selectOne(String sql, Object... args) {
		return SqlHelper.getObject(selectList(sql, args));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<Map<String, Object>> selectPage(Page page, String sql, Object... args) {
		if (null == page) {
			return null;
		}
		page.setRecords(sqlSession().selectList(SELECT_LIST, sqlMap(sql, args), page));
		return page;
	}

	/**
	 * <p>
	 * 获取Session 默认自动提交
	 * <p/>
	 */
	private SqlSession sqlSession() {
		return (clazz != null) ? SqlHelper.sqlSession(clazz)
				: GlobalConfigUtils.getSqlSession(FACTORY.getConfiguration());
	}

}
