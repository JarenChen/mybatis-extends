package com.wshsoft.mybatis.test.proxy;

import java.lang.reflect.Proxy;

/**
 * <p>
 * 类似 org.apache.ibatis.binding.MapperProxyFactory<T>
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-07-06
 */
public class MapperProxyFactory {

	public static <T> T getMapper(Class<T> type) {
		return newInstance(type);
	}

	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<T> methodInterface) {
		final MapperProxy<T> methodProxy = new MapperProxy<T>(methodInterface);
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
				new Class[] { methodInterface }, methodProxy);
	}

}
