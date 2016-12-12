package com.wshsoft.mybatis.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.wshsoft.mybatis.entity.GlobalConfiguration;
import com.wshsoft.mybatis.entity.TableInfo;
import com.wshsoft.mybatis.plugins.pagination.Pagination;
import com.wshsoft.mybatis.toolkit.CollectionUtils;
import com.wshsoft.mybatis.toolkit.StringUtils;
import com.wshsoft.mybatis.toolkit.TableInfoHelper;

/**
 * <p>
 * SqlQuery 执行 SQL
 * </p>
 *
 * @author Carry xie
 * @Date 2016-12-11
 */
public class SqlQuery {
	private static final Log logger = LogFactory.getLog(SqlQuery.class);
	// 单例Query
	public static final SqlQuery SQL_QUERY = new SqlQuery();
	private SqlSessionFactory sqlSessionFactory;
	private TableInfo tableInfo;

	public SqlQuery() {
		this.tableInfo = TableInfoHelper.getRandomTableInfo();
		String configMark = tableInfo.getConfigMark();
		GlobalConfiguration globalConfiguration = GlobalConfiguration.GlobalConfig(configMark);
		this.sqlSessionFactory = globalConfiguration.getSqlSessionFactory();
	}

	public SqlQuery(Class<?> clazz) {
		this.tableInfo = SqlHelper.table(clazz);
		GlobalConfiguration globalConfiguration = GlobalConfiguration.GlobalConfig(tableInfo.getConfigMark());
		this.sqlSessionFactory = globalConfiguration.getSqlSessionFactory();
	}

	public boolean insert(String sql, Object... args) {
		return SqlHelper.retBool(sqlSession().insert(sqlStatement("insertSql"), StringUtils.sqlArgsFill(sql, args)));
	}

	public boolean delete(String sql, Object... args) {
		return SqlHelper.retBool(sqlSession().delete(sqlStatement("deleteSql"), StringUtils.sqlArgsFill(sql, args)));
	}

	public boolean update(String sql, Object... args) {
		return SqlHelper.retBool(sqlSession().update(sqlStatement("updateSql"), StringUtils.sqlArgsFill(sql, args)));
	}

	public List<Map<String, Object>> selectList(String sql, Object... args) {
		return sqlSession().selectList(sqlStatement("selectListSql"), StringUtils.sqlArgsFill(sql, args));
	}

	public int selectCount(String sql, Object... args) {
		return sqlSession().<Integer>selectOne(sqlStatement("selectCountSql"), StringUtils.sqlArgsFill(sql, args));
	}

	public Map<String, Object> selectOne(String sql, Object... args) {
		List<Map<String, Object>> list = selectList(sql, args);
		if (CollectionUtils.isNotEmpty(list)) {
			int size = list.size();
			if (size > 1) {
				logger.warn(String.format("Warn: selectOne Method There are  %s results.", size));
			}
			return list.get(0);
		}
		return Collections.emptyMap();
	}

	public List<Map<String, Object>> selectPage(Pagination page, String sql, Object... args) {
		if (null == page) {
			return null;
		}
		return sqlSession().selectList(sqlStatement("selectPageSql"), StringUtils.sqlArgsFill(sql, args), page);
	}

	/**
	 * 获取默认的SqlQuery(适用于单库)
	 * 
	 * @return
	 */
	public static SqlQuery db() {
		return SQL_QUERY;
	}

	/**
	 * 根据当前class对象获取SqlQuery(适用于多库)
	 * 
	 * @param clazz
	 * @return
	 */
	public static SqlQuery db(Class<?> clazz) {
		return new SqlQuery(clazz);
	}

	/**
	 * <p>
	 * 获取Session 默认自动提交
	 * <p/>
	 */
	private SqlSession sqlSession() {
		return sqlSessionFactory.openSession(true);
	}

	private String sqlStatement(String sqlMethod) {
		return tableInfo.getSqlStatement(sqlMethod);
	}

}
