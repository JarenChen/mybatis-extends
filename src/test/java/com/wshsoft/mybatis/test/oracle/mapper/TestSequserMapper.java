package com.wshsoft.mybatis.test.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.wshsoft.mybatis.mapper.BaseMapper;
import com.wshsoft.mybatis.test.oracle.entity.TestSequser;

/**
 * TestUser 表数据库控制层接口
 */
public interface TestSequserMapper extends BaseMapper<TestSequser> {

	@Select("select * from TEST_SEQUSER")
	List<TestSequser> getList();
}