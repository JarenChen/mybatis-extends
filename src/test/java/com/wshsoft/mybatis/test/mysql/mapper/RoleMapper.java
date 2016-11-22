package com.wshsoft.mybatis.test.mysql.mapper;

import org.apache.ibatis.annotations.Delete;

import com.wshsoft.mybatis.mapper.BaseMapper;
import com.wshsoft.mybatis.test.mysql.entity.Role;

/**
 * @author Carry xie
 * @Date 2016-09-09
 */
public interface RoleMapper extends BaseMapper<Role> {
	@Delete("DELETE FROM role") 
	public void deleteAll();

}
