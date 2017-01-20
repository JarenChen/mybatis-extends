package com.wshsoft.mybatis.test.mysql.entity;

import com.wshsoft.mybatis.annotations.TableName;

import java.io.Serializable;

/**
 * <p>
 * 测试实体没有主键依然注入通用方法
 * </p>
 *
 * @author Carry xie
 * @Date 2016-12-22
 */
@TableName("not_pk")
public class NotPK implements Serializable {

	// 静态属性会自动忽略
	private static final long serialVersionUID = 1L;

	private String uuid;

	private String type;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
