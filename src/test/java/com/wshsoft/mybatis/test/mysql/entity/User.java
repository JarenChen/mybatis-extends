package com.wshsoft.mybatis.test.mysql.entity;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;

import com.wshsoft.mybatis.annotations.TableField;
import com.wshsoft.mybatis.annotations.TableId;
import com.wshsoft.mybatis.annotations.TableLogic;
import com.wshsoft.mybatis.annotations.TableName;
import com.wshsoft.mybatis.annotations.Version;
import com.wshsoft.mybatis.enums.FieldFill;

/**
 * <p>
 * 测试用户类
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-09-09
 */
/* 表名 value 注解【 驼峰命名可无 】, resultMap 注解测试【 映射 xml 的 resultMap 内容 】 */
@TableName(resultMap = "userMap")
public class User implements Serializable {

	/* 表字段注解，false 表中不存在的字段，可无该注解 默认 true */
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/* 主键ID 注解，value 字段名，type 用户输入ID */
	@TableId(value = "test_id")
	private Long id;

	/* 测试忽略验证 */
	private String name;

	private Integer age;

	/* BigDecimal 测试 */
	private BigDecimal price;

	/* 测试下划线字段命名类型, 字段填充 */
	@TableField(value = "test_type", fill = FieldFill.INSERT)
	@TableLogic(value = "-2") // 该注解为了测试逻辑删除、这里设置 -2 为删除值
	private Integer testType;

	@TableField(el = "role.id", value = "role_id")
	private Role role;

	private String desc = "默认描述";

	@Version
	private Integer version;

	// 或@TableField(el = "role,jdbcType=BIGINT)
	@TableField(el = "phone, typeHandler=com.wshsoft.mybatis.test.mysql.typehandler.PhoneTypeHandler")
	private PhoneNumber phone;

	private Date birthday;

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public User() {

	}

	public User(String name) {
		this.name = name;
	}

	public User(Integer testType) {
		this.testType = testType;
	}

	public User(String name, Integer age) {
		this.name = name;
		this.age = age;
	}

	public User(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public User(Long id, Integer age) {
		this.id = id;
		this.age = age;
	}

	public User(Long id, String name, Integer age, Integer testType) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.testType = testType;
	}

	public User(String name, Integer age, Integer testType) {
		this.name = name;
		this.age = age;
		this.testType = testType;
	}

	public User(Long id, String name, Integer age, Integer testType, Date birthday) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.testType = testType;
		this.birthday = birthday;
	}

	public User(String name, Integer age, Integer testType, Date birthday) {
		this.name = name;
		this.age = age;
		this.testType = testType;
		this.birthday = birthday;
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getTestType() {
		return testType;
	}

	public void setTestType(Integer testType) {
		this.testType = testType;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public PhoneNumber getPhone() {
		return phone;
	}

	public void setPhone(PhoneNumber phone) {
		this.phone = phone;
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

	@Override
	public String toString() {
		return "User{" + "id=" + id + ", name='" + name + '\'' + ", age=" + age + ", testType=" + testType + ", role="
				+ role + ", phone=" + phone + ", desc=" + desc + '}';
	}

	/**
	 * 测试类型
	 */
	public static void main(String args[]) throws IllegalAccessException {
		User user = new User();
		user.setName("12306");
		user.setAge(3);
		System.err.println(User.class.getName());
		Field[] fields = user.getClass().getDeclaredFields();
		for (Field field : fields) {
			System.out.println("===================================");
			System.out.println(field.getName());
			System.out.println(field.getType().toString());
			field.setAccessible(true);
			System.out.println(field.get(user));
		}
	}
}
