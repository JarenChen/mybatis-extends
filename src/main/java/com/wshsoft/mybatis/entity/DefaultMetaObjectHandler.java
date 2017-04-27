package com.wshsoft.mybatis.entity;

import com.wshsoft.mybatis.mapper.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

/**
 * <p>
 * 默认填充器关闭操作
 * </p>
 *
 * @author Carry xie
 * @since 2017-04-19
 */
public class DefaultMetaObjectHandler extends MetaObjectHandler {

	@Override
	public void insertFill(MetaObject metaObject) {

	}

	@Override
	public void updateFill(MetaObject metaObject) {

	}

	@Override
	public boolean openInsertFill() {
		return false;
	}

	@Override
	public boolean openUpdateFill() {
		return false;
	}

}
