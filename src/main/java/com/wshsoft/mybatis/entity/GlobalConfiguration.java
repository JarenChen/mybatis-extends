package com.wshsoft.mybatis.entity;

import com.wshsoft.mybatis.enums.DBType;
import com.wshsoft.mybatis.enums.FieldStrategy;
import com.wshsoft.mybatis.enums.IdType;
import com.wshsoft.mybatis.exceptions.MybatisExtendsException;
import com.wshsoft.mybatis.mapper.AutoSqlInjector;
import com.wshsoft.mybatis.mapper.IMetaObjectHandler;
import com.wshsoft.mybatis.mapper.ISqlInjector;
import com.wshsoft.mybatis.toolkit.JdbcUtils;
import com.wshsoft.mybatis.toolkit.TableInfoHelper;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
/**
 * <p>
 * Mybatis全局缓存
 * </p>
 *
 * @author Carry xie
 * @Date 2016-12-06
 */
@SuppressWarnings("serial")
public class GlobalConfiguration implements Cloneable, Serializable {

	// 日志
	private static final Log logger = LogFactory.getLog(GlobalConfiguration.class);
	/**
	 * 缓存全局信息
	 */
	private static final Map<String, GlobalConfiguration> GLOBAL_CONFIG = new ConcurrentHashMap<String, GlobalConfiguration>();
	/**
	 * 默认参数
	 */
	public static final GlobalConfiguration DEFAULT = new GlobalConfiguration(new AutoSqlInjector());

	// 数据库类型（默认 MySql）
	private DBType dbType = DBType.MYSQL;
	// 主键类型（默认 ID_WORKER）
	private IdType idType = IdType.ID_WORKER;
	// 表字段使用下划线命名（默认 false）
	private boolean dbColumnUnderline = false;
	// SQL注入器
	private ISqlInjector sqlInjector;
	// 元对象字段填充控制器
	private IMetaObjectHandler metaObjectHandler = null;
	// 字段验证策略
	private FieldStrategy fieldStrategy = FieldStrategy.NOT_NULL;
	// 是否刷新mapper
	private boolean isRefresh = false;
	// 是否自动获取DBType
	private boolean isAutoSetDbType = true;
	// 是否大写命名
	private boolean isCapitalMode = false;
	// 缓存当前Configuration的SqlSessionFactory
	private SqlSessionFactory sqlSessionFactory;

	private Set<String> mapperRegistryCache = new ConcurrentSkipListSet<String>();

	public GlobalConfiguration() {
		// TODO
	}

	public GlobalConfiguration(ISqlInjector sqlInjector) {
		this.sqlInjector = sqlInjector;
	}

	public DBType getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = DBType.getDBType(dbType);
		this.isAutoSetDbType = false;
	}

	public void setDbTypeByJdbcUrl(String jdbcUrl) {
		this.dbType = JdbcUtils.getDbType(jdbcUrl);
	}

	public IdType getIdType() {
		return idType;
	}

	public void setIdType(int idType) {
		this.idType = IdType.getIdType(idType);
	}

	public boolean isDbColumnUnderline() {
		return dbColumnUnderline;
	}

	public void setDbColumnUnderline(boolean dbColumnUnderline) {
		this.dbColumnUnderline = dbColumnUnderline;
	}

	public ISqlInjector getSqlInjector() {
		return sqlInjector;
	}

	public void setSqlInjector(ISqlInjector sqlInjector) {
		this.sqlInjector = sqlInjector;
	}

	public IMetaObjectHandler getMetaObjectHandler() {
		return metaObjectHandler;
	}

	public void setMetaObjectHandler(IMetaObjectHandler metaObjectHandler) {
		this.metaObjectHandler = metaObjectHandler;
	}

	public FieldStrategy getFieldStrategy() {
		return fieldStrategy;
	}

	public void setFieldStrategy(int fieldStrategy) {
		this.fieldStrategy = FieldStrategy.getFieldStrategy(fieldStrategy);
	}

	public boolean isRefresh() {
		return isRefresh;
	}

	public void setRefresh(boolean refresh) {
		this.isRefresh = refresh;
	}

	public boolean isAutoSetDbType() {
		return isAutoSetDbType;
	}

	public void setAutoSetDbType(boolean autoSetDbType) {
		this.isAutoSetDbType = autoSetDbType;
	}

	public Set<String> getMapperRegistryCache() {
		return mapperRegistryCache;
	}

	public void setMapperRegistryCache(Set<String> mapperRegistryCache) {
		this.mapperRegistryCache = mapperRegistryCache;
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	protected GlobalConfiguration clone() throws CloneNotSupportedException {
		return (GlobalConfiguration) super.clone();
	}

	/**
	 * 获取当前的SqlSessionFactory
	 * 
	 * @param clazz
	 * @return
	 */
	public static SqlSessionFactory currentSessionFactory(Class<?> clazz) {
		String configMark = TableInfoHelper.getTableInfo(clazz).getConfigMark();
		GlobalConfiguration mybatisGlobalConfig = GlobalConfiguration.GlobalConfig(configMark);
		return mybatisGlobalConfig.getSqlSessionFactory();
	}

	/**
	 * 获取默认MybatisGlobalConfig
	 * 
	 * @return
	 */
	public static GlobalConfiguration defaults() {
		try {
			return DEFAULT.clone();
		} catch (CloneNotSupportedException e) {
			throw new MybatisExtendsException("ERROR: CLONE MybatisGlobalCache DEFAULT FAIL !  Cause:" + e);
		}
	}

	/**
	 * <p>
	 * 设置全局设置(以configuration地址值作为Key)
	 * <p/>
	 *
	 * @param configuration
	 * @param mybatisGlobalConfig
	 * @return
	 */
	public static void setGlobalConfig(Configuration configuration, GlobalConfiguration mybatisGlobalConfig) {
		if (configuration == null || mybatisGlobalConfig == null) {
			new MybatisExtendsException("Error:  Could not setGlobalConfig");
		}
		// 设置全局设置
		GLOBAL_CONFIG.put(configuration.toString(), mybatisGlobalConfig);
	}

	/**
	 * <p>
	 * 设置全局设置 (统一所有入口)
	 * <p/>
	 *
	 * @param configuration
	 * @return
	 */
	public void setGlobalConfig(Configuration configuration) {
		setGlobalConfig(configuration, this);
	}

	/**
	 * 获取MybatisGlobalConfig (统一所有入口)
	 * 
	 * @param configuration
	 * @return
	 */
	public static GlobalConfiguration GlobalConfig(Configuration configuration) {
		if (configuration == null) {
			throw new MybatisExtendsException("Error: You need Initialize MybatisConfiguration !");
		}
		return GlobalConfig(configuration.toString());
	}

	/**
	 * 获取MybatisGlobalConfig (统一所有入口)
	 * 
	 * @param configMark
	 * @return
	 */
	public static GlobalConfiguration GlobalConfig(String configMark) {
		GlobalConfiguration cache = GLOBAL_CONFIG.get(configMark);
		if (cache == null) {
			// 没有获取全局配置初始全局配置
			logger.warn("Warn: Not getting global configuration ! global configuration Initializing !");
			GLOBAL_CONFIG.put(configMark, DEFAULT);
			return DEFAULT;
		}
		return cache;
	}

	public static DBType getDbType(Configuration configuration) {
		return GlobalConfig(configuration).getDbType();
	}

	public static IdType getIdType(Configuration configuration) {
		return GlobalConfig(configuration).getIdType();
	}

	public static boolean isDbColumnUnderline(Configuration configuration) {
		return GlobalConfig(configuration).isDbColumnUnderline();
	}

	public static ISqlInjector getSqlInjector(Configuration configuration) {
		return GlobalConfig(configuration).getSqlInjector();
	}

	public static IMetaObjectHandler getMetaObjectHandler(Configuration configuration) {
		return GlobalConfig(configuration).getMetaObjectHandler();
	}

	public static FieldStrategy getFieldStrategy(Configuration configuration) {
		return GlobalConfig(configuration).getFieldStrategy();
	}

	public static boolean isRefresh(Configuration configuration) {
		return GlobalConfig(configuration).isRefresh();
	}

	public static boolean isAutoSetDbType(Configuration configuration) {
		return GlobalConfig(configuration).isAutoSetDbType();
	}

	public static Set<String> getMapperRegistryCache(Configuration configuration) {
		return GlobalConfig(configuration).getMapperRegistryCache();
	}

	public boolean isCapitalMode() {
		return isCapitalMode;
	}

	public void setCapitalMode(boolean isCapitalMode) {
		this.isCapitalMode = isCapitalMode;
	}

}