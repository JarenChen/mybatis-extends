package com.wshsoft.mybatis.test.h2.entity.persistent;

import com.wshsoft.mybatis.annotations.TableField;
import com.wshsoft.mybatis.annotations.TableId;
import com.wshsoft.mybatis.annotations.TableName;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * h2address entity.
 * </p>
 *
 * @author Carry xie
 * @date 2017/5/25
 */
@Data
@Accessors(chain = true)
@TableName("h2address")
public class H2Addr {

    @TableId("addr_id")
    private Long addrId;

    @TableField("addr_name")
    private String addrName;

    @TableField("test_id")
    private Long testId;

}
