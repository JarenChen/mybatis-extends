package com.wshsoft.mybatis.test.h2.entity.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.wshsoft.mybatis.mapper.BaseMapper;
import com.wshsoft.mybatis.plugins.Page;
import com.wshsoft.mybatis.test.h2.entity.persistent.H2Addr;
import com.wshsoft.mybatis.test.h2.entity.persistent.H2User;

/**
 * <p>
 * TODO class
 * </p>
 *
 * @author Carry xie
 * @date 2017/4/1
 */
public interface H2UserMapper extends BaseMapper<H2User> {

    @Select(
            "select a.addr_id as addrId, a.addr_name as addrName from h2address a" +
                    " join h2user u on u.test_id=a.test_id and u.test_id=#{userId}"
    )
    public List<H2Addr> getAddrListByUserId(@Param("userId") Long userId);

    @Select(
            "select a.addr_id as addrId, a.addr_name as addrName from h2address a" +
                    " join h2user u on u.test_id=a.test_id and u.test_id=#{userId}"
    )
    public List<H2Addr> getAddrListByUserId(@Param("userId") Long userId, Page<H2Addr> page);

}
