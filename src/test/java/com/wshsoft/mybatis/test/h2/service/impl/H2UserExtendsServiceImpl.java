package com.wshsoft.mybatis.test.h2.service.impl;

import org.springframework.stereotype.Service;

import com.wshsoft.mybatis.service.impl.ServiceImpl;
import com.wshsoft.mybatis.test.h2.entity.mapper.H2UserExtendsMapper;
import com.wshsoft.mybatis.test.h2.entity.persistent.H2UserIntVersionExtendTO;
import com.wshsoft.mybatis.test.h2.service.IH2UserExtendsService;

/**
 * <p>
 * </p>
 *
 * @author Carry xie
 * @date 2017/6/26
 */
@Service
public class H2UserExtendsServiceImpl extends ServiceImpl<H2UserExtendsMapper, H2UserIntVersionExtendTO>
		implements IH2UserExtendsService {

}
