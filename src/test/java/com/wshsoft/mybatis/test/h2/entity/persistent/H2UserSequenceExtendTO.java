package com.wshsoft.mybatis.test.h2.entity.persistent;

import java.math.BigDecimal;

import com.wshsoft.mybatis.annotations.TableField;
import com.wshsoft.mybatis.annotations.TableName;
import com.wshsoft.mybatis.annotations.Version;
import com.wshsoft.mybatis.enums.FieldStrategy;

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
public class H2UserSequenceExtendTO extends BaseSequenceEntity {

	public H2UserSequenceExtendTO() {
	}

	public H2UserSequenceExtendTO(String name, Integer version) {
		this.name = name;
		this.version = version;
	}

	/* 测试忽略验证 */
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
