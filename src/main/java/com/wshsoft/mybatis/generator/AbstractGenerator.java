package com.wshsoft.mybatis.generator;

import com.wshsoft.mybatis.generator.config.DataSourceConfig;
import com.wshsoft.mybatis.generator.config.PackageConfig;
import com.wshsoft.mybatis.generator.config.StrategyConfig;
import com.wshsoft.mybatis.generator.config.TemplateConfig;
import com.wshsoft.mybatis.generator.config.builder.ConfigBuilder;

/**
 * 插件基类，用于属性配置 设计成抽象类主要是用于后期可扩展，共享参数配置。
 *
 * @author Carry xie
 * @since 2016/8/30
 */
public abstract class AbstractGenerator {

	
	/**
	 * 数据源配置
	 */
	private DataSourceConfig dataSource;

	/**
	 * 数据库表配置
	 */
	private StrategyConfig strategy;

	/**
	 * 包 相关配置
	 */
	private PackageConfig packageInfo;

	/**
	 * 模板 相关配置
	 */
	private TemplateConfig template;

	/**
	 * 生成文件的输出目录
	 */
	private String outputDir;

	/**
	 * 是否覆盖已有文件
	 */
	private boolean fileOverride = false;

	/**
	 * 是否打开输出目录
	 */
	private boolean open = true;

	/**
	 * 是否在xml中添加二级缓存配置
	 */
	private boolean enableCache = true;

	/**
	 * 开发人员
	 */
	private String author;

	/**
	 * 开启 ActiveRecord 模式
	 */
	private boolean activeRecord = true;

	protected ConfigBuilder config;

	/**
	 * 初始化配置
	 */
	protected void initConfig() {
		if (null == config) {
			config = new ConfigBuilder(packageInfo, dataSource, strategy, template, outputDir);
		}
	}

	public DataSourceConfig getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSourceConfig dataSource) {
		this.dataSource = dataSource;
	}

	public StrategyConfig getStrategy() {
		return strategy;
	}

	public void setStrategy(StrategyConfig strategy) {
		this.strategy = strategy;
	}

	public PackageConfig getPackageInfo() {
		return packageInfo;
	}

	public void setPackageInfo(PackageConfig packageInfo) {
		this.packageInfo = packageInfo;
	}

	public TemplateConfig getTemplate() {
		return template;
	}

	public void setTemplate(TemplateConfig template) {
		this.template = template;
	}

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public boolean isFileOverride() {
		return fileOverride;
	}

	public void setFileOverride(boolean fileOverride) {
		this.fileOverride = fileOverride;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isEnableCache() {
		return enableCache;
	}

	public void setEnableCache(boolean enableCache) {
		this.enableCache = enableCache;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public boolean isActiveRecord() {
		return activeRecord;
	}

	public void setActiveRecord(boolean activeRecord) {
		this.activeRecord = activeRecord;
	}

	public ConfigBuilder getConfig() {
		return config;
	}

	public void setConfig(ConfigBuilder config) {
		this.config = config;
	}

}
