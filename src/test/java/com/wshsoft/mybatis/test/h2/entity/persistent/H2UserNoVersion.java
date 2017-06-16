package com.wshsoft.mybatis.test.h2.entity.persistent;

import java.io.Serializable;
import java.math.BigDecimal;

import com.wshsoft.mybatis.annotations.TableField;
import com.wshsoft.mybatis.annotations.TableId;
import com.wshsoft.mybatis.annotations.TableName;
import com.wshsoft.mybatis.enums.FieldStrategy;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 测试用户类
 * </p>
 *
 * @author Carry xie
 */
/* 表名 value 注解【 驼峰命名可无 】, resultMap 注解测试【 映射 xml 的 resultMap 内容 】 */
@Data
@Accessors(chain = true)
@TableName("h2user")
public class H2UserNoVersion implements Serializable {

    /* 表字段注解，false 表中不存在的字段，可无该注解 默认 true */
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /* 主键ID 注解，value 字段名，type 用户输入ID */
    @TableId(value = "test_id")
    private Long id;

    /* 测试忽略验证 */
    private String name;

    private Integer age;

    /*BigDecimal 测试*/
    private BigDecimal price;

    /* 测试下划线字段命名类型, 字段填充 */
    @TableField(value = "test_type", validate = FieldStrategy.IGNORED)
    private Integer testType;

    private String desc;

    private Integer version;


    public H2UserNoVersion() {

    }

    public H2UserNoVersion(String name) {
        this.name = name;
    }

    public H2UserNoVersion(Integer testType) {
        this.testType = testType;
    }

    public H2UserNoVersion(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public H2UserNoVersion(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public H2UserNoVersion(Long id, Integer age) {
        this.id = id;
        this.age = age;
    }

    public H2UserNoVersion(Long id, String name, Integer age, Integer testType) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.testType = testType;
    }

    public H2UserNoVersion(String name, Integer age, Integer testType) {
        this.name = name;
        this.age = age;
        this.testType = testType;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
