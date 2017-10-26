package com.wshsoft.mybatis.test.h2.service;

import com.wshsoft.mybatis.service.IService;
import com.wshsoft.mybatis.test.h2.entity.persistent.H2UserLogicDelete;

/**
 * <p>
 * </p>
 *
 * @author Carry xie
 * @date 2017/6/15
 */
public interface IH2UserLogicDeleteService extends IService<H2UserLogicDelete> {

	public H2UserLogicDelete selectByIdMy(Long id);
}
