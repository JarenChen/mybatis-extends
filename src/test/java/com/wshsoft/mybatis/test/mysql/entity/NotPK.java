package com.wshsoft.mybatis.test.mysql.entity;

import java.io.Serializable;

import com.wshsoft.mybatis.annotations.TableField;
import com.wshsoft.mybatis.annotations.TableName;

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

	@TableField("is1")
	private boolean isIs;
	@TableField("is2")
	private Boolean isis;

	private String type;

	public Boolean getIsis() {
		return isis;
	}

	public void setIsis(Boolean isis) {
		this.isis = isis;
	}

	public boolean isIs() {
		return isIs;
	}

	public void setIs(boolean is) {
		isIs = is;
	}

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
