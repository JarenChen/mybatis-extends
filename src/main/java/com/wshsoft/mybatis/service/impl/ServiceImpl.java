package com.wshsoft.mybatis.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.wshsoft.mybatis.entity.TableInfo;
import com.wshsoft.mybatis.enums.SqlMethod;
import com.wshsoft.mybatis.exceptions.MybatisExtendsException;
import com.wshsoft.mybatis.mapper.BaseMapper;
import com.wshsoft.mybatis.mapper.Condition;
import com.wshsoft.mybatis.mapper.SqlHelper;
import com.wshsoft.mybatis.mapper.Wrapper;
import com.wshsoft.mybatis.plugins.Page;
import com.wshsoft.mybatis.service.IService;
import com.wshsoft.mybatis.toolkit.CollectionUtils;
import com.wshsoft.mybatis.toolkit.MapUtils;
import com.wshsoft.mybatis.toolkit.ReflectionKit;
import com.wshsoft.mybatis.toolkit.StringUtils;
import com.wshsoft.mybatis.toolkit.TableInfoHelper;

/**
 * <p>
 * IService 实现类（ 泛型：M 是 mapper 对象，T 是实体 ， PK 是主键泛型 ）
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-04-20
 */
public class ServiceImpl<M extends BaseMapper<T>, T> implements IService<T> {

	private static final Log logger = LogFactory.getLog(ServiceImpl.class);

	@Autowired
	protected M baseMapper;

	/**
	 * <p>
	 * 判断数据库操作是否成功
	 * </p>
	 * <p>
	 * 注意！！ 该方法为 Integer 判断，不可传入 int 基本类型
	 * </p>
	 *
	 * @param result
	 *            数据库操作返回影响条数
	 * @return boolean
	 */
	protected static boolean retBool(Integer result) {
		return SqlHelper.retBool(result);
	}

    @SuppressWarnings("unchecked")
    protected Class<T> currentModelClass() {
        return ReflectionKit.getSuperClassGenricType(getClass(), 1);
    }

    /**
     * <p>
     * 批量操作 SqlSession
     * </p>
     */
    protected SqlSession sqlSessionBatch() {
        return SqlHelper.sqlSessionBatch(currentModelClass());
    }

    /**
     * 获取SqlStatement
     *
     * @param sqlMethod
     * @return
     */
    protected String sqlStatement(SqlMethod sqlMethod) {
        return SqlHelper.table(currentModelClass()).getSqlStatement(sqlMethod.getMethod());
    }

	@Override
	@Transactional
	public boolean insert(T entity) {
		return retBool(baseMapper.insert(entity));
	}

	@Override
	@Transactional
	public boolean insertAllColumn(T entity) {
		return retBool(baseMapper.insertAllColumn(entity));
	}

	@Override
	@Transactional
	public boolean insertBatch(List<T> entityList) {
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
	@Transactional
	public boolean insertBatch(List<T> entityList, int batchSize) {
		if (CollectionUtils.isEmpty(entityList)) {
			throw new IllegalArgumentException("Error: entityList must not be empty");
		}
		try (SqlSession batchSqlSession = sqlSessionBatch()) {
			int size = entityList.size();
			String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
			for (int i = 0; i < size; i++) {
				batchSqlSession.insert(sqlStatement, entityList.get(i));
				if (i >= 1 && i % batchSize == 0) {
					batchSqlSession.flushStatements();
				}
			}
			batchSqlSession.flushStatements();
		} catch (Throwable e) {
        	throw new MybatisExtendsException("Error: Cannot execute insertBatch Method. Cause", e);

		}
		return true;

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
	@Transactional
	public boolean insertOrUpdate(T entity) {
		if (null != entity) {
			Class<?> cls = entity.getClass();
			TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
			if (null != tableInfo && StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
				Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
				if (StringUtils.checkValNull(idVal)) {
					return insert(entity);
				} else {
					/*
					 * 更新成功直接返回，失败执行插入逻辑
					 */
					return updateById(entity) || insert(entity);
				}
			} else {
				throw new MybatisExtendsException("Error:  Can not execute. Could not find @TableId.");
			}
		}
		return false;
	}

	@Override
	@Transactional
	public boolean insertOrUpdateBatch(List<T> entityList) {
		return insertOrUpdateBatch(entityList, 30);
	}

	@Override
	@Transactional
	public boolean insertOrUpdateBatch(List<T> entityList, int batchSize) {
		if (CollectionUtils.isEmpty(entityList)) {
			throw new IllegalArgumentException("Error: entityList must not be empty");
		}
		try (SqlSession batchSqlSession = sqlSessionBatch()) {
			int size = entityList.size();
			for (int i = 0; i < size; i++) {
				insertOrUpdate(entityList.get(i));
				if (i >= 1 && i % batchSize == 0) {
					batchSqlSession.flushStatements();
				}
			}
			batchSqlSession.flushStatements();
		} catch (Throwable e) {
			 throw new MybatisExtendsException("Error: Cannot execute insertBatch Method. Cause", e);
		}
		return true;
	}

	@Override
	@Transactional
	public boolean deleteById(Serializable id) {
		return retBool(baseMapper.deleteById(id));
	}

	@Override
	@Transactional
	public boolean deleteByMap(Map<String, Object> columnMap) {
		if (MapUtils.isEmpty(columnMap)) {
			throw new MybatisExtendsException("deleteByMap columnMap is empty.");
		}
		return retBool(baseMapper.deleteByMap(columnMap));
	}

	@Override
	@Transactional
	public boolean delete(Wrapper<T> wrapper) {
		return retBool(baseMapper.delete(wrapper));
	}

	@Override
	@Transactional
	public boolean deleteBatchIds(List<? extends Serializable> idList) {
		return retBool(baseMapper.deleteBatchIds(idList));
	}

	@Override
	@Transactional
	public boolean updateById(T entity) {
		return retBool(baseMapper.updateById(entity));
	}

	@Override
	@Transactional
	public boolean updateAllColumnById(T entity) {
		return retBool(baseMapper.updateAllColumnById(entity));
	}

	@Override
	@Transactional
	public boolean update(T entity, Wrapper<T> wrapper) {
		return retBool(baseMapper.update(entity, wrapper));
	}

	@Override
	@Transactional
	public boolean updateBatchById(List<T> entityList) {
		return updateBatchById(entityList, 30);
	}

	@Override
	@Transactional
	public boolean updateBatchById(List<T> entityList, int batchSize) {
		if (CollectionUtils.isEmpty(entityList)) {
			throw new IllegalArgumentException("Error: entityList must not be empty");
		}
		try (SqlSession batchSqlSession = sqlSessionBatch()) {
			int size = entityList.size();
			String sqlStatement = sqlStatement(SqlMethod.UPDATE_BY_ID);
			for (int i = 0; i < size; i++) {
                MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
                param.put("et",entityList.get(i));
                batchSqlSession.update(sqlStatement, param);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
            }
            batchSqlSession.flushStatements();
        } catch (Throwable e) {
            throw new MybatisExtendsException("Error: Cannot execute insertBatch Method. Cause", e);
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
		return SqlHelper.getObject(baseMapper.selectList(wrapper));
	}

	@Override
	public Map<String, Object> selectMap(Wrapper<T> wrapper) {
		return SqlHelper.getObject(baseMapper.selectMaps(wrapper));
	}

	@Override
	public Object selectObj(Wrapper<T> wrapper) {
		return SqlHelper.getObject(baseMapper.selectObjs(wrapper));
	}

	@Override
	public int selectCount(Wrapper<T> wrapper) {
		return SqlHelper.retCount(baseMapper.selectCount(wrapper));
	}

	@Override
	public List<T> selectList(Wrapper<T> wrapper) {
		return baseMapper.selectList(wrapper);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Page<T> selectPage(Page<T> page) {
		return selectPage(page, Condition.EMPTY);
	}

	@Override
	public List<Map<String, Object>> selectMaps(Wrapper<T> wrapper) {
		return baseMapper.selectMaps(wrapper);
	}

	@Override
	public List<Object> selectObjs(Wrapper<T> wrapper) {
		return baseMapper.selectObjs(wrapper);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Page<Map<String, Object>> selectMapsPage(Page page, Wrapper<T> wrapper) {
		SqlHelper.fillWrapper(page, wrapper);
		page.setRecords(baseMapper.selectMapsPage(page, wrapper));
		return page;
	}

	@Override
	public Page<T> selectPage(Page<T> page, Wrapper<T> wrapper) {
		SqlHelper.fillWrapper(page, wrapper);
		page.setRecords(baseMapper.selectPage(page, wrapper));
		return page;
	}

}
