package com.wshsoft.mybatis.test.h2.entity.service.impl;

import org.springframework.stereotype.Service;

import com.wshsoft.mybatis.service.impl.ServiceImpl;
import com.wshsoft.mybatis.test.h2.entity.mapper.H2UserNoVersionMapper;
import com.wshsoft.mybatis.test.h2.entity.persistent.H2UserNoVersion;
import com.wshsoft.mybatis.test.h2.entity.service.IH2UserNoVersionService;

/**
 * <p>
 * Service层测试
 * </p>
 *
 * @author Carry xie
 * @date 2017-01-30
 */
@Service
public class H2UserNoVersionServiceImpl extends ServiceImpl<H2UserNoVersionMapper, H2UserNoVersion> implements IH2UserNoVersionService {

}
