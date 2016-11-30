package com.wshsoft.framework.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.wshsoft.mybatis.mapper.Wrapper;
import com.wshsoft.mybatis.plugins.Page;

/**
 * <p>
 * 顶级 Service
 * </p>
 *
 * @author Carry xie
 * @Date 2016-04-20
 */
public interface IService<T> {

	/**
	 * <p>
	 * 插入一条记录
	 * </p>
	 *
	 * @param entity
	 *            实体对象
	 * @return boolean
	 */
	boolean insert(T entity);

	/**
	 * <p>
	 * 插入（批量），该方法不适合 Oracle
	 * </p>
	 *
	 * @param entityList
	 *            实体对象列表
	 * @return boolean
	 */
	boolean insertBatch(List<T> entityList);

	/**
	 * <p>
	 * 插入（批量）
	 * </p>
	 *
	 * @param entityList
	 *            实体对象列表
	 * @param batchSize
	 *
	 * @return boolean
	 */
	boolean insertBatch(List<T> entityList, int batchSize);

	/**
	 * <p>
	 * 批量修改插入
	 * </p>
	 *
	 * @param entityList
	 *            实体对象列表
	 * @return boolean
	 */
	boolean insertOrUpdateBatch(List<T> entityList);

	/**
	 * <p>
	 * 批量修改插入
	 * </p>
	 *
	 * @param entityList
	 *            实体对象列表
	 * @param batchSize
	 *
	 * @return boolean
	 */
	boolean insertOrUpdateBatch(List<T> entityList, int batchSize);

	/**
	 * <p>
	 * 根据 ID 删除
	 * </p>
	 *
	 * @param id
	 *            主键ID
	 * @return boolean
	 */
	boolean deleteById(Serializable id);

	/**
	 * <p>
	 * 根据 columnMap 条件，删除记录
	 * </p>
	 *
	 * @param columnMap
	 *            表字段 map 对象
	 * @return boolean
	 */
	boolean deleteByMap(Map<String, Object> columnMap);

	/**
	 * <p>
	 * 根据 entity 条件，删除记录
	 * </p>
	 *
	 * @param wrapper
	 *            实体包装类 {@link Wrapper}
	 * @return boolean
	 */
	boolean delete(Wrapper<T> wrapper);

	/**
	 * <p>
	 * 删除（根据ID 批量删除）
	 * </p>
	 *
	 * @param idList
	 *            主键ID列表
	 * @return boolean
	 */
	boolean deleteBatchIds(List<? extends Serializable> idList);

	/**
	 * <p>
	 * 根据 ID 修改
	 * </p>
	 *
	 * @param entity
	 *            实体对象
	 * @return boolean
	 */
	boolean updateById(T entity);

	/**
	 * <p>
	 * 根据 whereEntity 条件，更新记录
	 * </p>
	 *
	 * @param entity
	 *            实体对象
	 * @param wrapper
	 *            实体包装类 {@link Wrapper}
	 * @return boolean
	 */
	boolean update(T entity, Wrapper<T> wrapper);

	/**
	 * <p>
	 * 根据ID 批量更新
	 * </p>
	 *
	 * @param entityList
	 *            实体对象列表
	 * @return boolean
	 */
	boolean updateBatchById(List<T> entityList);

	/**
	 * <p>
	 * TableId 注解存在更新记录，否插入一条记录
	 * </p>
	 *
	 * @param entity
	 *            实体对象
	 * @return boolean
	 */
	boolean insertOrUpdate(T entity);

	/**
	 * <p>
	 * 根据 ID 查询
	 * </p>
	 *
	 * @param id
	 *            主键ID
	 * @return T
	 */
	T selectById(Serializable id);

	/**
	 * <p>
	 * 查询（根据ID 批量查询）
	 * </p>
	 *
	 * @param idList
	 *            主键ID列表
	 * @return List<T>
	 */
	List<T> selectBatchIds(List<? extends Serializable> idList);

	/**
	 * <p>
	 * 查询（根据 columnMap 条件）
	 * </p>
	 *
	 * @param columnMap
	 *            表字段 map 对象
	 * @return List<T>
	 */
	List<T> selectByMap(Map<String, Object> columnMap);

	/**
	 * <p>
	 * 根据 Wrapper，查询一条记录
	 * </p>
	 *
	 * @param wrapper
	 *            实体对象
	 * @return T
	 */
	T selectOne(Wrapper<T> wrapper);

	/**
	 * <p>
	 * 根据 Wrapper 条件，查询总记录数
	 * </p>
	 *
	 * @param wrapper
	 *            实体对象
	 * @return int
	 */
	int selectCount(Wrapper<T> wrapper);

	/**
	 * <p>
	 * 查询列表
	 * </p>
	 *
	 * @param wrapper
	 *            实体包装类 {@link Wrapper}
	 * @return
	 */
	List<T> selectList(Wrapper<T> wrapper);

	/**
	 * <p>
	 * 翻页查询
	 * </p>
	 *
	 * @param page
	 *            翻页对象
	 * @return
	 */
	Page<T> selectPage(Page<T> page);

	/**
	 * <p>
	 * 翻页查询
	 * </p>
	 *
	 * @param page
	 *            翻页对象
	 * @param wrapper
	 *            实体包装类 {@link Wrapper}
	 * @return
	 */
	Page<T> selectPage(Page<T> page, Wrapper<T> wrapper);
}