package com.wshsoft.mybatis.generator.config;

import com.wshsoft.mybatis.generator.config.po.TableInfo;

/**
 * <p>
 * 输出文件配置
 * </p>
 * 
 * @author Carry xie
 * @since 2017-01-18
 */
public abstract class FileOutConfig {
	/**
	 * 模板
	 */
	private String templatePath;

	public FileOutConfig() {

	}

	public FileOutConfig(String templatePath) {
		this.templatePath = templatePath;
	}

	/**
	 * 输出文件
	 */
	public abstract String outputFile(TableInfo tableInfo);

	public String getTemplatePath() {
		return templatePath;
	}

	public FileOutConfig setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
		return this;
	}

}
