package com.wshsoft.mybatis.activerecord;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;

import com.wshsoft.mybatis.exceptions.MybatisExtendsException;
import com.wshsoft.mybatis.toolkit.TableInfo;
import com.wshsoft.mybatis.toolkit.TableInfoHelper;

/**
 * <p>
 * ActiveRecord 模式 CRUD
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-11-06
 */
public class Record {

	/**
	 * 获取Session 默认自动提交
	 * <p>
	 * 特别说明:这里获取SqlSession时这里虽然设置了自动提交但是如果事务托管了的话 是不起作用的 切记!!
	 * <p/>
	 *
	 * @return SqlSession
	 */
	public static SqlSession sqlSession(Class<?> clazz) {
		return sqlSession(clazz, true);
	}

	/**
	 * <p>
	 * 批量操作 SqlSession
	 * </p>
	 * 
	 * @param clazz
	 *            实体类
	 * @return SqlSession
	 */
	public static SqlSession sqlSessionBatch(Class<?> clazz) {
		return table(clazz).getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
	}

	/**
	 * <p>
	 * 获取Session
	 * </p>
	 * 
	 * @param clazz
	 *            实体类
	 * @param autoCommit
	 *            true自动提交false则相反
	 * @return SqlSession
	 */
	public static SqlSession sqlSession(Class<?> clazz, boolean autoCommit) {
		return table(clazz).getSqlSessionFactory().openSession(autoCommit);
	}

	/**
	 * 获取TableInfo
	 * 
	 * @return TableInfo
	 */
	public static TableInfo table(Class<?> clazz) {
		TableInfo tableInfo = TableInfoHelper.getTableInfo(clazz);
		if (null == tableInfo) {
			throw new MybatisExtendsException("Error: Cannot execute table Method, ClassGenricType not found .");
		}
		return tableInfo;
	}

}
