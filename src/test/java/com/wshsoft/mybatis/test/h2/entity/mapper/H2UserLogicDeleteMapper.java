package com.wshsoft.mybatis.test.h2.entity.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.wshsoft.mybatis.mapper.BaseMapper;
import com.wshsoft.mybatis.test.h2.entity.persistent.H2UserLogicDelete;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author Carry xie
 * @date 2017/6/15
 */
public interface H2UserLogicDeleteMapper extends BaseMapper<H2UserLogicDelete> {

	@Insert("insert into h2user(name,version) values(#{name},#{version})")
	public int myInsertWithNameVersion(@Param("name") String name, @Param("version") int version);

	@Update("update h2user set name=#{name} where test_id=#{id}")
	public int myUpdateWithNameId(@Param("id") Long id, @Param("name") String name);

	@Select("select test_id as id, name, age, version, test_type as testType from h2user where test_id=#{id}")
	H2UserLogicDelete selectByIdMy(@Param("id") Long id);
}
