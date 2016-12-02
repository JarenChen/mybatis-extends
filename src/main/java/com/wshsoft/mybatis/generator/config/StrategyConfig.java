package com.wshsoft.mybatis.generator.config;

import com.wshsoft.mybatis.generator.config.rules.IdStrategy;
import com.wshsoft.mybatis.generator.config.rules.NamingStrategy;

/**
 * 策略配置项
 *
 * @author Carry xie
 * @since 2016/8/30
 */
public class StrategyConfig {

	/**
	 * 数据库表映射到实体的命名策略
	 */
	private NamingStrategy naming = NamingStrategy.nochange;

	private NamingStrategy fieldNaming;

	/**
	 * 表前缀
	 */
	private String tablePrefix;

	/**
	 * Entity 中的ID生成类型
	 */
	private IdStrategy idGenType = IdStrategy.id_worker;

	/**
	 * 自定义继承的Entity类全称，带包名
	 */
	private String superEntityClass;

	/**
	 * 自定义继承的Mapper类全称，带包名
	 */
	private String superMapperClass = ConstVal.SUPERD_MAPPER_CLASS;

	/**
	 * 自定义继承的Service类全称，带包名
	 */
	private String superServiceClass = ConstVal.SUPERD_SERVICE_CLASS;

	/**
	 * 自定义继承的ServiceImpl类全称，带包名
	 */
	private String superServiceImplClass = ConstVal.SUPERD_SERVICEIMPL_CLASS;

	/**
	 * 自定义继承的Controller类全称，带包名
	 */
	private String superControllerClass;

	/*
	 * 需要包含的表名（与exclude二选一配置）
	 */
	private String[] include = null;

	/**
	 * 需要排除的表名
	 */
	private String[] exclude = null;

	public NamingStrategy getNaming() {
		return naming;
	}

	public void setNaming(NamingStrategy naming) {
		this.naming = naming;
	}

	public NamingStrategy getFieldNaming() {
		return fieldNaming;
	}

	public void setFieldNaming(NamingStrategy fieldNaming) {
		this.fieldNaming = fieldNaming;
	}

	public String getTablePrefix() {
		return tablePrefix;
	}

	public void setTablePrefix(String tablePrefix) {
		this.tablePrefix = tablePrefix;
	}

	public IdStrategy getIdGenType() {
		return idGenType;
	}

	public void setIdGenType(IdStrategy idGenType) {
		this.idGenType = idGenType;
	}

	public String getSuperEntityClass() {
		return superEntityClass;
	}

	public void setSuperEntityClass(String superEntityClass) {
		this.superEntityClass = superEntityClass;
	}

	public String getSuperMapperClass() {
		return superMapperClass;
	}

	public void setSuperMapperClass(String superMapperClass) {
		this.superMapperClass = superMapperClass;
	}

	public String getSuperServiceClass() {
		return superServiceClass;
	}

	public void setSuperServiceClass(String superServiceClass) {
		this.superServiceClass = superServiceClass;
	}

	public String getSuperServiceImplClass() {
		return superServiceImplClass;
	}

	public void setSuperServiceImplClass(String superServiceImplClass) {
		this.superServiceImplClass = superServiceImplClass;
	}

	public String getSuperControllerClass() {
		return superControllerClass;
	}

	public void setSuperControllerClass(String superControllerClass) {
		this.superControllerClass = superControllerClass;
	}

	public String[] getInclude() {
		return include;
	}

	public void setInclude(String[] include) {
		this.include = include;
	}

	public String[] getExclude() {
		return exclude;
	}

	public void setExclude(String[] exclude) {
		this.exclude = exclude;
	}

}
