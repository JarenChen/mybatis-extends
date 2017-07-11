package com.wshsoft.mybatis.test.h2.entity.persistent;

import java.math.BigDecimal;
import java.util.Date;

import com.wshsoft.mybatis.annotations.TableField;
import com.wshsoft.mybatis.annotations.TableName;
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
public class H2UserIntVersionExtendTO extends BaseIntVersionEntity {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /* 测试忽略验证 */
    private String name;

    private Integer age;

    /*BigDecimal 测试*/
    private BigDecimal price;

    /* 测试下划线字段命名类型, 字段填充 */
    @TableField(value = "test_type", strategy = FieldStrategy.IGNORED)
    private Integer testType;

    private String desc;
	@TableField("last_updated_dt")
    private Date lastUpdatedDt;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getTestType() {
		return testType;
	}

	public void setTestType(Integer testType) {
		this.testType = testType;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

}
