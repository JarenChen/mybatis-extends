package com.wshsoft.mybatis.spring;

import com.wshsoft.mybatis.MybatisConfiguration;
import com.wshsoft.mybatis.toolkit.SystemClock;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.builder.xml.XMLMapperEntityResolver;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.parsing.XPathParser;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.core.NestedIOException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * <p>
 * 切莫用于生产环境（后果自负）<br>
 * Mybatis 映射文件热加载（发生变动后自动重新加载）.<br>
 * 方便开发时使用，不用每次修改xml文件后都要去重启应用.<br>
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-08-25
 */
public class MybatisMapperRefresh implements Runnable {
	protected final Logger logger = Logger.getLogger("MybatisMapperRefresh");
	private SqlSessionFactory sqlSessionFactory;
	private Resource[] mapperLocations;
	private Long beforeTime = 0L;
	private Configuration configuration;

	/**
	 * 是否开启刷新mapper
	 */
	private boolean enabled;

	/**
	 * xml文件目录
	 */
	private Set<String> fileSet;

	/**
	 * 延迟加载时间
	 */
	private int delaySeconds = 10;

	/**
	 * 刷新间隔时间
	 */
	private int sleepSeconds = 20;

	/**
	 * 记录jar包存在的mapper
	 */
	private static Map<String, List<Resource>> jarMapper = new HashMap<String, List<Resource>>();

	public MybatisMapperRefresh(Resource[] mapperLocations, SqlSessionFactory sqlSessionFactory, int delaySeconds,
			int sleepSeconds, boolean enabled) {
		this.mapperLocations = mapperLocations;
		this.sqlSessionFactory = sqlSessionFactory;
		this.delaySeconds = delaySeconds;
		this.enabled = enabled;
		this.sleepSeconds = sleepSeconds;
		this.run();
	}

	public MybatisMapperRefresh(Resource[] mapperLocations, SqlSessionFactory sqlSessionFactory, boolean enabled) {
		this.mapperLocations = mapperLocations;
		this.sqlSessionFactory = sqlSessionFactory;
		this.enabled = enabled;
		this.run();
	}

	@Override
	public void run() {
		/*
		 * 启动 XML 热加载
		 */
		if (enabled) {
			beforeTime = SystemClock.now();
			final MybatisMapperRefresh runnable = this;
			new Thread(new Runnable() {

				@Override
				public void run() {
					if (fileSet == null) {
						fileSet = new HashSet<String>();
						for (Resource mapperLocation : mapperLocations) {
							try {
								if (ResourceUtils.isJarURL(mapperLocation.getURL())) {
									String key = new UrlResource(ResourceUtils.extractJarFileURL(mapperLocation.getURL())).getFile().getPath();
									fileSet.add(key);
									if (jarMapper.get(key) != null) {
										jarMapper.get(key).add(mapperLocation);
									} else {
										List<Resource> resourcesList = new ArrayList<Resource>();
										resourcesList.add(mapperLocation);
										jarMapper.put(key, resourcesList);
									}
								} else {
									fileSet.add(mapperLocation.getFile().getPath());
								}
							} catch (IOException ioException) {
								ioException.printStackTrace();
							}
						}
					}
					try {
						Thread.sleep(delaySeconds * 1000);
					} catch (InterruptedException interruptedException) {
						interruptedException.printStackTrace();
					}
					while (true) {
						try {
							for (String filePath : fileSet) {
								File file = new File(filePath);
								if (file != null && file.isFile() && file.lastModified() > beforeTime) {
									MybatisConfiguration.IS_REFRESH = true;
									List<Resource> removeList = jarMapper.get(filePath);
									if (removeList != null && !removeList.isEmpty()) {// 如果是jar包中的xml，将刷新jar包中存在的所有xml，后期再修改加载jar中修改过后的xml
										for (Resource resource : removeList) {
											runnable.refresh(resource);
										}
									} else {
										runnable.refresh(new FileSystemResource(file));
									}
								}
							}
							if (MybatisConfiguration.IS_REFRESH) {
								beforeTime = SystemClock.now();
							}
							MybatisConfiguration.IS_REFRESH = false;
						} catch (Exception exception) {
							exception.printStackTrace();
						}
						try {
							Thread.sleep(sleepSeconds * 1000);
						} catch (InterruptedException interruptedException) {
							interruptedException.printStackTrace();
						}

					}
				}
			}, "mybatis-extends MapperRefresh").start();
		}
	}

	/**
	 * 刷新mapper
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private void refresh(Resource resource) throws Exception {
		this.configuration = sqlSessionFactory.getConfiguration();
		boolean isSupper = configuration.getClass().getSuperclass() == Configuration.class;
		try {
			Field loadedResourcesField = isSupper
					? configuration.getClass().getSuperclass().getDeclaredField("loadedResources")
					: configuration.getClass().getDeclaredField("loadedResources");
			loadedResourcesField.setAccessible(true);
			Set loadedResourcesSet = ((Set) loadedResourcesField.get(configuration));
			XPathParser xPathParser = new XPathParser(resource.getInputStream(), true, configuration.getVariables(),
					new XMLMapperEntityResolver());
			XNode context = xPathParser.evalNode("/mapper");
			String namespace = context.getStringAttribute("namespace");
			Field field = MapperRegistry.class.getDeclaredField("knownMappers");
			field.setAccessible(true);
			Map mapConfig = (Map) field.get(configuration.getMapperRegistry());
			mapConfig.remove(Resources.classForName(namespace));
			loadedResourcesSet.remove(resource.toString());
			configuration.getCacheNames().remove(namespace);
			cleanParameterMap(context.evalNodes("/mapper/parameterMap"), namespace);
			cleanResultMap(context.evalNodes("/mapper/resultMap"), namespace);
			cleanKeyGenerators(context.evalNodes("insert|update"), namespace);
			cleanSqlElement(context.evalNodes("/mapper/sql"), namespace);
			XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(resource.getInputStream(),
					sqlSessionFactory.getConfiguration(), // 注入的sql先不进行处理了
					resource.toString(), sqlSessionFactory.getConfiguration().getSqlFragments());
			xmlMapperBuilder.parse();
			logger.fine("refresh:" + resource + ",success!");
		} catch (Exception e) {
			throw new NestedIOException("Failed to parse mapping resource: '" + resource + "'", e);
		} finally {
			ErrorContext.instance().reset();
		}
	}

	/**
	 * 清理parameterMap
	 * 
	 * @param list
	 * @param namespace
	 */
	private void cleanParameterMap(List<XNode> list, String namespace) {
		for (XNode parameterMapNode : list) {
			String id = parameterMapNode.getStringAttribute("id");
			configuration.getParameterMaps().remove(namespace + "." + id);
		}
	}

	/**
	 * 清理resultMap
	 * 
	 * @param list
	 * @param namespace
	 */
	private void cleanResultMap(List<XNode> list, String namespace) {
		for (XNode resultMapNode : list) {
			String id = resultMapNode.getStringAttribute("id", resultMapNode.getValueBasedIdentifier());
			configuration.getResultMapNames().remove(id);
			configuration.getResultMapNames().remove(namespace + "." + id);
		}
	}

	/**
	 * 清理selectKey
	 * 
	 * @param list
	 * @param namespace
	 */
	private void cleanKeyGenerators(List<XNode> list, String namespace) {
		for (XNode context : list) {
			String id = context.getStringAttribute("id");
			configuration.getKeyGeneratorNames().remove(id + SelectKeyGenerator.SELECT_KEY_SUFFIX);
			configuration.getKeyGeneratorNames().remove(namespace + "." + id + SelectKeyGenerator.SELECT_KEY_SUFFIX);
		}
	}

	/**
	 * 清理sql节点缓存
	 * 
	 * @param list
	 * @param namespace
	 */
	private void cleanSqlElement(List<XNode> list, String namespace) {
		for (XNode context : list) {
			String id = context.getStringAttribute("id");
			configuration.getSqlFragments().remove(id);
			configuration.getSqlFragments().remove(namespace + "." + id);
		}
	}

}