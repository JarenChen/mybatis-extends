package com.wshsoft.mybatis.toolkit;

import java.lang.reflect.Proxy;
import java.util.Properties;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

/**
 * 插件工具类
 * 
 * @author Carry xie
 */
public final class PluginUtils {

	public static final String DELEGATE_BOUNDSQL_SQL = "delegate.boundSql.sql";
	public static final String DELEGATE_MAPPEDSTATEMENT = "delegate.mappedStatement";

	private PluginUtils() {
		// to do nothing
	}

	/**
	 * <p>
	 * 获取当前执行 MappedStatement
	 * </p>
	 *
	 * @param metaObject
	 *            元对象
	 * @return
	 */
	public static MappedStatement getMappedStatement(MetaObject metaObject) {
		return (MappedStatement) metaObject.getValue(DELEGATE_MAPPEDSTATEMENT);
	}

	/**
	 * <p>
	 * 获得真正的处理对象,可能多层代理.
	 * </p>
	 */
	public static Object realTarget(Object target) {
		if (Proxy.isProxyClass(target.getClass())) {
			MetaObject metaObject = SystemMetaObject.forObject(target);
			return realTarget(metaObject.getValue("h.target"));
		}
		return target;
	}

	/**
	 * <p>
	 * 根据 key 获取 Properties 的值
	 * </p>
	 */
	public static String getProperty(Properties properties, String key) {
		String value = properties.getProperty(key);
		return StringUtils.isEmpty(value) ? null : value;
	}
}
