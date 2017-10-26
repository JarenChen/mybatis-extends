package com.wshsoft.mybatis.test.h2.entity;

import java.io.Serializable;

import com.wshsoft.mybatis.annotations.TableId;

/**
 * <p>
 * 测试父类情况
 * </p>
 *
 * @author Carry xie
 * @Date 2016-06-26
 */
public class SuperEntity implements Serializable {

	/* 主键ID 注解，value 字段名，type 用户输入ID */
	@TableId(value = "test_id")
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
