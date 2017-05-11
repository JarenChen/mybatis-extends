package com.wshsoft.mybatis.test.plugins.optimisticLocker.entity;

import com.wshsoft.mybatis.annotations.Version;

public class IntVersionFather {
	@Version
	private Integer version;

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
