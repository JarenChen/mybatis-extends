package com.wshsoft.mybatis.test.plugins.optimisticLocker.entity;

import java.io.Serializable;

import com.wshsoft.mybatis.annotations.TableField;
import com.wshsoft.mybatis.annotations.TableName;
import com.wshsoft.mybatis.annotations.Version;

@TableName("version_user")
public class IntVersionUser implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	private Long id;

	private String name;

	private Integer age;

	@Version
	private Integer version;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
