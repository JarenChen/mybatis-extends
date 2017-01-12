package com.wshsoft.mybatis.generator.config.po;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.wshsoft.mybatis.toolkit.CollectionUtils;
import com.wshsoft.mybatis.toolkit.StringUtils;

/**
 * <p>
 * 表信息，关联到当前字段信息
 * </p>
 *
 * @author Carry xie
 * @since 2016/8/30
 */
public class TableInfo {

	private String name;
	private String comment;

	private String entityName;
	private String mapperName;
	private String xmlName;
	private String serviceName;
	private String serviceImplName;
	private String controllerName;

	private List<TableField> fields;
	private List<String> importPackages = new ArrayList<String>();
	private String fieldNames;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getEntityPath() {
		StringBuffer ep = new StringBuffer();
		ep.append(entityName.substring(0, 1).toLowerCase());
		ep.append(entityName.substring(1));
		return ep.toString();
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getMapperName() {
		return mapperName;
	}

	public void setMapperName(String mapperName) {
		this.mapperName = mapperName;
	}

	public String getXmlName() {
		return xmlName;
	}

	public void setXmlName(String xmlName) {
		this.xmlName = xmlName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceImplName() {
		return serviceImplName;
	}

	public void setServiceImplName(String serviceImplName) {
		this.serviceImplName = serviceImplName;
	}

	public String getControllerName() {
		return controllerName;
	}

	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}

	public List<TableField> getFields() {
		return fields;
	}

	public void setFields(List<TableField> fields) {
		if (CollectionUtils.isNotEmpty(fields)) {
			this.fields = fields;
			// 收集导入包信息
			Set<String> pkgSet = new HashSet<String>();
			for (TableField field : fields) {
				if (null != field.getColumnType() && null != field.getColumnType().getPkg()) {
					pkgSet.add(field.getColumnType().getPkg());
				}
				if (field.isKeyFlag() && field.isConvert()) {
					pkgSet.add("com.wshsoft.mybatis.annotations.TableId");
				}
				if (field.isConvert()) {
					pkgSet.add("com.wshsoft.mybatis.annotations.TableField");
				}
			}
			if (!pkgSet.isEmpty()) {
				this.importPackages =  new ArrayList(Arrays.asList(pkgSet.toArray(new String[] {})));
			}
		}
	}

	public List<String> getImportPackages() {
		return importPackages;
	}

	public void setImportPackages(String pkg) {
		importPackages.add(pkg);
	}

	/**
	 * 转换filed实体为xmlmapper中的basecolumn字符串信息
	 *
	 * @return
	 */
	public String getFieldNames() {
		if (StringUtils.isEmpty(fieldNames)) {
			StringBuilder names = new StringBuilder();
			for (int i = 0; i < fields.size(); i++) {
				TableField fd = fields.get(i);
				if (i == fields.size() - 1) {
					names.append(cov2col(fd));
				} else {
					names.append(cov2col(fd)).append(", ");
				}
			}
			fieldNames = names.toString();
		}
		return fieldNames;
	}

	/**
	 * mapper xml中的字字段添加as
	 *
	 * @param field
	 *            字段实体
	 * @return 转换后的信息
	 */
	private String cov2col(TableField field) {
		if (null != field) {
			return field.isConvert() ? field.getName() + " AS " + field.getPropertyName() : field.getName();
		}
		return StringUtils.EMPTY;
	}

}
