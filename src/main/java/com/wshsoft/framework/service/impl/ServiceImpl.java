package com.wshsoft.framework.service.impl;

import com.wshsoft.framework.service.IService;
import com.wshsoft.mybatis.activerecord.Record;
import com.wshsoft.mybatis.annotations.IdType;
import com.wshsoft.mybatis.exceptions.MybatisExtendsException;
import com.wshsoft.mybatis.mapper.BaseMapper;
import com.wshsoft.mybatis.mapper.Wrapper;
import com.wshsoft.mybatis.plugins.Page;
import com.wshsoft.mybatis.toolkit.CollectionUtil;
import com.wshsoft.mybatis.toolkit.ReflectionKit;
import com.wshsoft.mybatis.toolkit.StringUtils;
import com.wshsoft.mybatis.toolkit.TableInfo;
import com.wshsoft.mybatis.toolkit.TableInfoHelper;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * <p>
 * IService 实现类（ 泛型：M 是 mapper 对象，T 是实体 ， PK 是主键泛型 ）
 * </p>
 *
 * @author Carry xie
 * @Date 2016-04-20
 */
public class ServiceImpl<M extends BaseMapper<T>, T> implements IService<T> {

	protected static final Logger logger = Logger.getLogger("ServiceImpl");

	@Autowired
	protected M baseMapper;

	/**
	 * 判断数据库操作是否成功
	 *
	 * @param result
	 *            数据库操作返回影响条数
	 * @return boolean
	 */
	protected boolean retBool(int result) {
		return result >= 1;
	}

	/**
	 * <p>
	 * SQL 构建方法
	 * </p>
	 * 
	 * @param sql
	 *            SQL 语句
	 * @param args
	 *            执行参数
	 * @return
	 */
	protected String sqlBuilder(SQL sql, Object... args) {
		if (null == sql) {
			throw new IllegalArgumentException("Error: sql Can not be empty.");
		}
		return StringUtils.sqlArgsFill(sql.toString(), args);
	}

	@SuppressWarnings("unchecked")
	protected Class<T> currentModleClass() {
		return ReflectionKit.getSuperClassGenricType(getClass(), 1);
	}

	/**
	 * <p>
	 * 批量操作 SqlSession
	 * </p>
	 */
	protected SqlSession sqlSessionBatch() {
		return Record.sqlSessionBatch(currentModleClass());
	}

	/**
	 * <p>
	 * TableId 注解存在更新记录，否插入一条记录
	 * </p>
	 *
	 * @param entity
	 *            实体对象
	 * @return boolean
	 */
	@Override
	public boolean insertOrUpdate(T entity) {
		if (null != entity) {
			Class<?> cls = entity.getClass();
			TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
			if (null != tableInfo) {
				Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
				if (StringUtils.checkValNull(idVal)) {
					return insert(entity);
				} else {
					/* 特殊处理 INPUT 主键策略逻辑 */
					if (IdType.INPUT == tableInfo.getIdType()) {
						T entityValue = selectById((Serializable) idVal);
						if (null != entityValue) {
							return updateById(entity);
						} else {
							return insert(entity);
						}
					}
					return updateById(entity);
				}
			} else {
				throw new MybatisExtendsException("Error:  Can not execute. Could not find @TableId.");
			}
		}
		return false;
	}

	@Override
	public boolean insert(T entity) {
		return retBool(baseMapper.insert(entity));
	}

	@Override
	public boolean insertBatch(List<T> entityList) {
		if (CollectionUtil.isEmpty(entityList)) {
			throw new IllegalArgumentException("Error: entityList must not be empty");
		}
		return insertBatch(entityList, 30);
	}

	/**
	 * 批量插入
	 *
	 * @param entityList
	 * @param batchSize
	 * @return
	 */
	@Override
	public boolean insertBatch(List<T> entityList, int batchSize) {
		if (CollectionUtil.isEmpty(entityList)) {
			throw new IllegalArgumentException("Error: entityList must not be empty");
		}
		SqlSession batchSqlSession = sqlSessionBatch();
		try {
			int size = entityList.size();
			for (int i = 0; i < size; i++) {
				baseMapper.insert(entityList.get(i));
				if (i % batchSize == 0) {
					batchSqlSession.flushStatements();
				}
			}
			batchSqlSession.flushStatements();
		} catch (Exception e) {
			logger.warning("Error: Cannot execute insertBatch Method. Cause:" + e);
			return false;
		}
		return true;

	}

	@Override
	public boolean deleteById(Serializable id) {
		return retBool(baseMapper.deleteById(id));
	}

	@Override
	public boolean deleteByMap(Map<String, Object> columnMap) {
		return retBool(baseMapper.deleteByMap(columnMap));
	}

	@Override
	public boolean delete(Wrapper<T> wrapper) {
		return retBool(baseMapper.delete(wrapper));
	}

	@Override
	public boolean deleteBatchIds(List<? extends Serializable> idList) {
		return retBool(baseMapper.deleteBatchIds(idList));
	}

	@Override
	public boolean updateById(T entity) {
		return retBool(baseMapper.updateById(entity));
	}

	@Override
	public boolean update(T entity, Wrapper<T> wrapper) {
		return retBool(baseMapper.update(entity, wrapper));
	}

	@Override
	public boolean updateBatchById(List<T> entityList) {
		if (CollectionUtil.isEmpty(entityList)) {
			throw new IllegalArgumentException("Error: entityList must not be empty");
		}
		SqlSession batchSqlSession = sqlSessionBatch();
		try {
			int size = entityList.size();
			for (int i = 0; i < size; i++) {
				baseMapper.updateById(entityList.get(i));
				if (i % 30 == 0) {
					batchSqlSession.flushStatements();
				}
			}
			batchSqlSession.flushStatements();
		} catch (Exception e) {
			logger.warning("Error: Cannot execute insertBatch Method. Cause:" + e);
			return false;
		}
		return true;
	}

	@Override
	public T selectById(Serializable id) {
		return baseMapper.selectById(id);
	}

	@Override
	public List<T> selectBatchIds(List<? extends Serializable> idList) {
		return baseMapper.selectBatchIds(idList);
	}

	@Override
	public List<T> selectByMap(Map<String, Object> columnMap) {
		return baseMapper.selectByMap(columnMap);
	}

	@Override
	public T selectOne(Wrapper<T> wrapper) {
		List<T> list = baseMapper.selectList(wrapper);
		if (CollectionUtil.isNotEmpty(list)) {
			int size = list.size();
			if (size > 1) {
				logger.warning(String.format("Warn: selectOne Method There are  %s results.", size));
			}
			return list.get(0);
		}
		return null;
	}

	@Override
	public int selectCount(Wrapper<T> wrapper) {
		return baseMapper.selectCount(wrapper);
	}

	@Override
	public List<T> selectList(Wrapper<T> wrapper) {
		return baseMapper.selectList(wrapper);
	}

	@Override
	public Page<T> selectPage(Page<T> page) {
		page.setRecords(baseMapper.selectPage(page, null));
		return page;
	}

	@Override
	public Page<T> selectPage(Page<T> page, Wrapper<T> wrapper) {
		if (null != wrapper) {
			wrapper.orderBy(page.getOrderByField(), page.isAsc());
		}
		page.setRecords(baseMapper.selectPage(page, wrapper));
		return page;
	}

}
