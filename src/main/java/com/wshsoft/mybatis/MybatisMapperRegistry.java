package com.wshsoft.mybatis;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import com.wshsoft.mybatis.toolkit.GlobalConfigUtils;

/**
 * <p>
 * 继承至MapperRegistry
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-09-26
 */
public class MybatisMapperRegistry extends MapperRegistry {

	private final Map<Class<?>, MapperProxyFactory<?>> knownMappers = new HashMap<>();
	private final Configuration config;

	public MybatisMapperRegistry(Configuration config) {
		super(config);
		this.config = config;
		// TODO注入SqlRunner
		GlobalConfigUtils.getSqlInjector(config).injectSqlRunner(config);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
		final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
		if (mapperProxyFactory == null) {
			throw new BindingException("Type " + type + " is not known to the MybatisExtendsMapperRegistry.");
		}
		try {
			return mapperProxyFactory.newInstance(sqlSession);
		} catch (Exception e) {
			throw new BindingException("Error getting mapper instance. Cause: " + e, e);
		}
	}

	@Override
	public <T> boolean hasMapper(Class<T> type) {
		return knownMappers.containsKey(type);
	}

	@Override
	public <T> void addMapper(Class<T> type) {
		if (type.isInterface()) {
			if (hasMapper(type)) {
				// TODO 如果之前注入 直接返回
				return;
				// throw new BindingException("Type " + type +
				// " is already known to the MybatisExtendsMapperRegistry.");
			}
			boolean loadCompleted = false;
			try {
				knownMappers.put(type, new MapperProxyFactory<T>(type));
				// It's important that the type is added before the parser is
				// run
				// otherwise the binding may automatically be attempted by the
				// mapper parser. If the type is already known, it won't try.

				// TODO 自定义无 XML 注入
				MybatisMapperAnnotationBuilder parser = new MybatisMapperAnnotationBuilder(config, type);
				parser.parse();
				loadCompleted = true;
			} finally {
				if (!loadCompleted) {
					knownMappers.remove(type);
				}
			}
		}
	}

	/**
	 * @since 3.2.2
	 */
	@Override
	public Collection<Class<?>> getMappers() {
		return Collections.unmodifiableCollection(knownMappers.keySet());
	}

}
