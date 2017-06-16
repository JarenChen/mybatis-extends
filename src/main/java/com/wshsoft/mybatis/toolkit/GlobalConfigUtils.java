package com.wshsoft.mybatis.toolkit;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.wshsoft.mybatis.entity.GlobalConfiguration;
import com.wshsoft.mybatis.enums.DBType;
import com.wshsoft.mybatis.enums.FieldStrategy;
import com.wshsoft.mybatis.enums.IdType;
import com.wshsoft.mybatis.exceptions.MybatisExtendsException;
import com.wshsoft.mybatis.incrementer.IKeyGenerator;
import com.wshsoft.mybatis.mapper.AutoSqlInjector;
import com.wshsoft.mybatis.mapper.ISqlInjector;
import com.wshsoft.mybatis.mapper.MetaObjectHandler;

/**
 * <p>
 * Mybatis全局缓存工具类
 * </p>
 *
 * @author Carry xie
 * @since 2017-06-15
 */
public class GlobalConfigUtils {

    /**
     * 默认参数
     */
    public static final GlobalConfiguration DEFAULT = defaults();
    // 日志
    private static final Log logger = LogFactory.getLog(GlobalConfigUtils.class);
    /**
     * 缓存全局信息
     */
    private static final Map<String, GlobalConfiguration> GLOBAL_CONFIG = new ConcurrentHashMap<>();

    public GlobalConfigUtils() {
        // 构造方法
    }

    /**
     * 获取当前的SqlSessionFactory
     *
     * @param clazz
     * @return
     */
    public static SqlSessionFactory currentSessionFactory(Class<?> clazz) {
        String configMark = TableInfoHelper.getTableInfo(clazz).getConfigMark();
        GlobalConfiguration mybatisGlobalConfig = GlobalConfigUtils.getGlobalConfig(configMark);
        return mybatisGlobalConfig.getSqlSessionFactory();
    }

    /**
     * 获取默认MybatisGlobalConfig
     *
     * @return
     */
    public static GlobalConfiguration defaults() {
        return new GlobalConfiguration();
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
            throw new MybatisExtendsException("Error: Could not setGlobalConfig");
        }
        // 设置全局设置
        GLOBAL_CONFIG.put(configuration.toString(), mybatisGlobalConfig);
    }

    /**
     * 获取MybatisGlobalConfig (统一所有入口)
     *
     * @param configuration
     * @return
     */
    public static GlobalConfiguration getGlobalConfig(Configuration configuration) {
        if (configuration == null) {
            throw new MybatisExtendsException("Error: You need Initialize MybatisConfiguration !");
        }
        return getGlobalConfig(configuration.toString());
    }

    /**
     * 获取MybatisGlobalConfig (统一所有入口)
     *
     * @param configMark
     * @return
     */
    public static GlobalConfiguration getGlobalConfig(String configMark) {
        GlobalConfiguration cache = GLOBAL_CONFIG.get(configMark);
        if (cache == null) {
            // 没有获取全局配置初始全局配置
            logger.debug("DeBug: MyBatis Plus Global configuration Initializing !");
            GLOBAL_CONFIG.put(configMark, DEFAULT);
            return DEFAULT;
        }
        return cache;
    }

    public static DBType getDbType(Configuration configuration) {
        return getGlobalConfig(configuration).getDbType();
    }

    public static IKeyGenerator getKeyGenerator(Configuration configuration) {
        return getGlobalConfig(configuration).getKeyGenerator();
    }

    public static IdType getIdType(Configuration configuration) {
        return getGlobalConfig(configuration).getIdType();
    }

    public static boolean isDbColumnUnderline(Configuration configuration) {
        return getGlobalConfig(configuration).isDbColumnUnderline();
    }

    public static ISqlInjector getSqlInjector(Configuration configuration) {
        // fix #140
        GlobalConfiguration globalConfiguration = getGlobalConfig(configuration);
        ISqlInjector sqlInjector = globalConfiguration.getSqlInjector();
        if (sqlInjector == null) {
            sqlInjector = new AutoSqlInjector();
            globalConfiguration.setSqlInjector(sqlInjector);
        }
        return sqlInjector;
    }

    public static MetaObjectHandler getMetaObjectHandler(Configuration configuration) {
        return getGlobalConfig(configuration).getMetaObjectHandler();
    }

    public static FieldStrategy getFieldStrategy(Configuration configuration) {
        return getGlobalConfig(configuration).getFieldStrategy();
    }

    public static boolean isRefresh(Configuration configuration) {
        return getGlobalConfig(configuration).isRefresh();
    }

    public static boolean isAutoSetDbType(Configuration configuration) {
        return getGlobalConfig(configuration).isAutoSetDbType();
    }

    public static Set<String> getMapperRegistryCache(Configuration configuration) {
        return getGlobalConfig(configuration).getMapperRegistryCache();
    }

    public static String getIdentifierQuote(Configuration configuration) {
        return getGlobalConfig(configuration).getIdentifierQuote();
    }

    public static SqlSession getSqlSession(Configuration configuration) {
        return getGlobalConfig(configuration).getSqlSession();
    }

    /**
     * 设置元数据相关属性
     *
     * @param dataSource
     * @param globalConfig
     */
    public static void setMetaData(DataSource dataSource, GlobalConfiguration globalConfig) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            String jdbcUrl = connection.getMetaData().getURL();
            // 设置全局关键字
            globalConfig.setSqlKeywords(connection.getMetaData().getSQLKeywords());
            // 自动设置数据库类型
            if (globalConfig.isAutoSetDbType()) {
                globalConfig.setDbTypeByJdbcUrl(jdbcUrl);
            }
        } catch (SQLException e) {
            logger.warn("Warn: GlobalConfiguration setMetaData Fail !  Cause:" + e);
        } finally {
            IOUtils.closeQuietly(connection);
        }
    }


}
