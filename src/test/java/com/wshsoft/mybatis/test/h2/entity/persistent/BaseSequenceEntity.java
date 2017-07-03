package com.wshsoft.mybatis.test.h2.entity.persistent;

import com.wshsoft.mybatis.annotations.KeySequence;
import com.wshsoft.mybatis.annotations.TableField;
import com.wshsoft.mybatis.annotations.TableId;
import com.wshsoft.mybatis.enums.IdType;

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
@KeySequence("SEQ_TEST")
public abstract class BaseSequenceEntity {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "TEST_ID", type = IdType.INPUT)
    private Long id;

}
