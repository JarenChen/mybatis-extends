package com.wshsoft.mybatis.activerecord;

import com.wshsoft.mybatis.enums.SqlMethod;
import com.wshsoft.mybatis.exceptions.MybatisExtendsException;
import com.wshsoft.mybatis.mapper.Condition;
import com.wshsoft.mybatis.mapper.SqlHelper;
import com.wshsoft.mybatis.mapper.SqlRunner;
import com.wshsoft.mybatis.mapper.Wrapper;
import com.wshsoft.mybatis.plugins.Page;
import com.wshsoft.mybatis.toolkit.CollectionUtils;
import com.wshsoft.mybatis.toolkit.StringUtils;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ActiveRecord 模式 CRUD
 * </p>
 * 
 * @author Carry xie
 * @param <T>
 * @Date 2016-11-06
 */
@SuppressWarnings({ "serial", "rawtypes" })
public abstract class Model<T extends Model> implements Serializable {

	private static final Log logger = LogFactory.getLog(Model.class);

	/**
	 * <p>
	 * 插入
	 * </p>
	 */
	public boolean insert() {
		return SqlHelper.retBool(sqlSession().insert(sqlStatement(SqlMethod.INSERT_ONE), this));
	}

	/**
	 * <p>
	 * 插入 OR 更新
	 * </p>
	 */
	public boolean insertOrUpdate() {
		if (StringUtils.checkValNotNull(pkVal())) {
			// update
			return SqlHelper.retBool(sqlSession().update(sqlStatement(SqlMethod.UPDATE_BY_ID), this));
		} else {
			// insert
			return SqlHelper.retBool(sqlSession().insert(sqlStatement(SqlMethod.INSERT_ONE), this));
		}
	}

	/**
	 * <p>
	 * 根据 ID 删除
	 * </p>
	 *
	 * @param id
	 *            主键ID
	 * @return
	 */
	public boolean deleteById(Serializable id) {
		return SqlHelper.retBool(sqlSession().delete(sqlStatement(SqlMethod.DELETE_BY_ID), id));
	}

	/**
	 * <p>
	 * 根据主键删除
	 * </p>
	 *
	 * @return
	 */
	public boolean deleteById() {
		if (StringUtils.checkValNull(pkVal())) {
			throw new MybatisExtendsException("deleteById primaryKey is null.");
		}
		return deleteById(this.pkVal());
	}

	/**
	 * <p>
	 * 删除记录
	 * </p>
	 *
	 * @param whereClause
	 *            查询条件
	 * @param args
	 *            查询条件值
	 * @return
	 */
	public boolean delete(String whereClause, Object... args) {
		return delete(Condition.instance().where(whereClause, args));
	}

	/**
	 * <p>
	 * 删除记录
	 * </p>
	 *
	 * @param wrapper
	 * @return
	 */
	public boolean delete(Wrapper wrapper) {
		Map<String, Object> map = new HashMap<String, Object>();
		// delete
		map.put("ew", wrapper);
		return SqlHelper.retBool(sqlSession().delete(sqlStatement(SqlMethod.DELETE), map));
	}

	/**
	 * <p>
	 * 更新
	 * </p>
	 *
	 * @return
	 */
	public boolean updateById() {
		if (StringUtils.checkValNull(pkVal())) {
			throw new MybatisExtendsException("updateById primaryKey is null.");
		}
		// updateById
		return SqlHelper.retBool(sqlSession().update(sqlStatement(SqlMethod.UPDATE_BY_ID), this));
	}

	/**
	 * <p>
	 * 执行 SQL 更新
	 * </p>
	 *
	 * @param whereClause
	 *            查询条件
	 * @param args
	 *            查询条件值
	 * @return
	 */
	public boolean update(String whereClause, Object... args) {
		// update
		return update(Condition.instance().where(whereClause, args));
	}

	/**
	 * <p>
	 * 执行 SQL 更新
	 * </p>
	 *
	 * @param wrapper
	 * @return
	 */
	public boolean update(Wrapper wrapper) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("et", this);
		map.put("ew", wrapper);
		// update
		return SqlHelper.retBool(sqlSession().update(sqlStatement(SqlMethod.UPDATE), map));
	}

	/**
	 * <p>
	 * 查询所有
	 * </p>
	 *
	 * @return
	 */
	public List<T> selectAll() {
		return sqlSession().selectList(sqlStatement(SqlMethod.SELECT_LIST));
	}

	/**
	 * <p>
	 * 根据 ID 查询
	 * </p>
	 *
	 * @param id
	 *            主键ID
	 * @return
	 */
	public T selectById(Serializable id) {
		return sqlSession().selectOne(sqlStatement(SqlMethod.SELECT_BY_ID), id);
	}

	/**
	 * <p>
	 * 根据主键查询
	 * </p>
	 *
	 * @return
	 */
	public T selectById() {
		if (StringUtils.checkValNull(pkVal())) {
			throw new MybatisExtendsException("selectById primaryKey is null.");
		}
		return selectById(this.pkVal());
	}

	/**
	 * <p>
	 * 查询总记录数
	 * </p>
	 *
	 * @param wrapper
	 * @return
	 */

	public List<T> selectList(Wrapper wrapper) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ew", wrapper);
		return sqlSession().selectList(sqlStatement(SqlMethod.SELECT_LIST), map);
	}

	/**
	 * <p>
	 * 查询所有
	 * </p>
	 *
	 * @param whereClause
	 * @param args
	 * @return
	 */
	public List<T> selectList(String whereClause, Object... args) {
		return selectList(Condition.instance().where(whereClause, args));
	}

	/**
	 * <p>
	 * 查询一条记录
	 * </p>
	 *
	 * @param wrapper
	 * @return
	 */
	public T selectOne(Wrapper wrapper) {
		List<T> list = selectList(wrapper);
		if (CollectionUtils.isNotEmpty(list)) {
			int size = list.size();
			if (size > 1) {
				logger.warn(String.format("Warn: selectOne Method There are  %s results.", size));
			}
			return list.get(0);
		}
		return null;
	}

	/**
	 * <p>
	 * 查询一条记录
	 * </p>
	 *
	 * @param whereClause
	 * @param args
	 * @return
	 */
	public T selectOne(String whereClause, Object... args) {
		return selectOne(Condition.instance().where(whereClause, args));
	}

	/**
	 * <p>
	 * 翻页查询
	 * </p>
	 *
	 * @param page
	 *            翻页查询条件
	 * @param wrapper
	 * @return
	 */
	public Page<T> selectPage(Page<T> page, Wrapper wrapper) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (wrapper != null && StringUtils.isNotEmpty(page.getOrderByField())) {
			wrapper.orderBy(page.getOrderByField());
		}
		map.put("ew", wrapper);
		List<T> tl = sqlSession().selectList(sqlStatement(SqlMethod.SELECT_PAGE), map, page);
		page.setRecords(tl);
		return page;
	}

	/**
	 * <p>
	 * 查询所有(分页)
	 * </p>
	 *
	 * @param page
	 * @param whereClause
	 * @param args
	 * @return
	 */
	public Page<T> selectPage(Page<T> page, String whereClause, Object... args) {
		return selectPage(page, Condition.instance().where(whereClause, args));
	}

	/**
	 * <p>
	 * 查询总数
	 * </p>
	 *
	 * @param whereClause
	 *            查询条件
	 * @param args
	 *            查询条件值
	 * @return
	 */
	public int selectCount(String whereClause, Object... args) {
		return selectCount(Condition.instance().where(whereClause, args));
	}

	/**
	 * <p>
	 * 查询总数
	 * </p>
	 *
	 * @param wrapper
	 * @return
	 */
	public int selectCount(Wrapper wrapper) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ew", wrapper);
		return SqlHelper.retCount(sqlSession().<Integer> selectOne(sqlStatement(SqlMethod.SELECT_COUNT), map));
	}

	/**
	 * <p>
	 * 执行 SQL
	 * </p>
	 */
	public SqlRunner sql() {
		return new SqlRunner(getClass());
	}

	/**
	 * <p>
	 * 获取Session 默认自动提交
	 * <p/>
	 */
	private SqlSession sqlSession() {
		return SqlHelper.sqlSession(getClass());
	}

	/**
	 * 获取SqlStatement
	 *
	 * @param sqlMethod
	 * @return
	 */
	private String sqlStatement(SqlMethod sqlMethod) {
		return sqlStatement(sqlMethod.getMethod());
	}

	private String sqlStatement(String sqlMethod) {
		return SqlHelper.table(getClass()).getSqlStatement(sqlMethod);
	}

	/**
	 * 主键值
	 */
	protected abstract Serializable pkVal();

}
