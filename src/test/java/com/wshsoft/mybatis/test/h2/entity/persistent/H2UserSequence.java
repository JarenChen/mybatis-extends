package com.wshsoft.mybatis.test.h2.entity.persistent;

import java.math.BigDecimal;

import com.wshsoft.mybatis.annotations.KeySequence;
import com.wshsoft.mybatis.annotations.TableField;
import com.wshsoft.mybatis.annotations.TableId;
import com.wshsoft.mybatis.annotations.TableName;
import com.wshsoft.mybatis.annotations.Version;
import com.wshsoft.mybatis.enums.FieldStrategy;
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
@TableName("h2user")
@KeySequence("SEQ_TEST")
public class H2UserSequence {

	public H2UserSequence() {
	}

	public H2UserSequence(String name, Integer version) {
		this.name = name;
		this.version = version;
	}

	/**
	 * 主键ID
	 */
	@TableId(value = "TEST_ID", type = IdType.INPUT)
	private Long id;

	private String name;

	private Integer age;

	/* BigDecimal 测试 */
	private BigDecimal price;

	/* 测试下划线字段命名类型, 字段填充 */
	@TableField(value = "test_type", strategy = FieldStrategy.IGNORED)
	private Integer testType;

	private String desc;

	@Version
	private Integer version;

}
