package com.wshsoft.mybatis.generator.config;

/**
 * 
 * @ClassName: TemplateConfig
 * @Description: 模板路径配置项
 * @author Carry xie
 * @date 2016年11月10日 下午4:45:12
 *
 */
public class TemplateConfig {

	private String entity = ConstVal.TEMPLATE_ENTITY;

	private String service = ConstVal.TEMPLATE_SERVICE;

	private String serviceImpl = ConstVal.TEMPLATE_SERVICEIMPL;

	private String mapper = ConstVal.TEMPLATE_MAPPER;

	private String xml = ConstVal.TEMPLATE_XML;

	private String controller = ConstVal.TEMPLATE_CONTROLLER;

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getServiceImpl() {
		return serviceImpl;
	}

	public void setServiceImpl(String serviceImpl) {
		this.serviceImpl = serviceImpl;
	}

	public String getMapper() {
		return mapper;
	}

	public void setMapper(String mapper) {
		this.mapper = mapper;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
	}

}
