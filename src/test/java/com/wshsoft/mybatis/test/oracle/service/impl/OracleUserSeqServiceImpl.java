package com.wshsoft.mybatis.test.oracle.service.impl;

import org.springframework.stereotype.Service;

import com.wshsoft.mybatis.service.impl.ServiceImpl;
import com.wshsoft.mybatis.test.oracle.entity.TestSequser;
import com.wshsoft.mybatis.test.oracle.mapper.TestSequserMapper;
import com.wshsoft.mybatis.test.oracle.service.OracleUserSeqService;

/**
 * <p>
 * </p>
 *
 * @author Carry xie
 * @date 2017/6/14
 */
@Service
public class OracleUserSeqServiceImpl extends ServiceImpl<TestSequserMapper, TestSequser>
		implements OracleUserSeqService {

}
