package com.wshsoft.mybatis.test.mysql.entity;

import java.io.Serializable;

import com.wshsoft.mybatis.activerecord.Model;

/**
 * <p>
 * 测试没有XML同样注入CRUD SQL 实体
 * </p>
 *
 * @author Carry xie
 * @Date 2016-09-25
 */
//表名为 test 可以不需要注解，特殊如 @TableName("tb_test")
public class Test extends Model<Test> {

	// 静态属性会自动忽略
	private static final long serialVersionUID = 1L;

	/** 主键 */
	// 默认会找 id 为主键，特殊命名需要注解 @TableId
	private Long id;

	private String type;

	public Test() {

	}

	public Test(Long id, String type) {
		this.id = id;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	protected Serializable getPrimaryKey() {
		return id;
	}

	@Override
	public String toString() {
		return "{id=" + id + ",type=" + type + "}";
	}
}