package com.wshsoft.mybatis.test.h2.service.impl;

import org.springframework.stereotype.Service;

import com.wshsoft.mybatis.service.impl.ServiceImpl;
import com.wshsoft.mybatis.test.h2.entity.mapper.H2UserSequenceMapper;
import com.wshsoft.mybatis.test.h2.entity.persistent.H2UserSequence;
import com.wshsoft.mybatis.test.h2.service.IH2UserSequenceService;

/**
 * <p>
 * </p>
 *
 * @author Carry xie
 * @date 2017/6/26
 */
@Service
public class H2UserSequenceServiceImpl extends ServiceImpl<H2UserSequenceMapper,H2UserSequence> implements IH2UserSequenceService {

}
