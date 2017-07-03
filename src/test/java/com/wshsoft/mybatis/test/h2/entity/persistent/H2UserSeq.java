package com.wshsoft.mybatis.test.h2.entity.persistent;

import com.wshsoft.mybatis.annotations.KeySequence;
import com.wshsoft.mybatis.annotations.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * </p>
 *
 * @author Carry xie
 * @date 2017/6/26
 */
@Data
@Accessors(chain = true)
@TableName("h2user")
@KeySequence("SEQ_TEST")
public class H2UserSeq {

}
