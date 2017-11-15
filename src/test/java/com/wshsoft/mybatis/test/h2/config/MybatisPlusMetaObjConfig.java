package com.wshsoft.mybatis.test.h2.config;

import javax.sql.DataSource;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import com.wshsoft.mybatis.MybatisConfiguration;
import com.wshsoft.mybatis.MybatisXMLLanguageDriver;
import com.wshsoft.mybatis.entity.GlobalConfiguration;
import com.wshsoft.mybatis.plugins.PaginationInterceptor;
import com.wshsoft.mybatis.plugins.PerformanceInterceptor;
import com.wshsoft.mybatis.spring.MybatisSqlSessionFactoryBean;
import com.wshsoft.mybatis.test.h2.H2MetaObjectHandler;

/**
 * <p>
 * Mybatis Plus Config without OptimisLock
 * </p>
 *
 * @author Carry xie
 * @date 2017/4/1
 */
@Configuration
@MapperScan("com.wshsoft.mybatis.test.h2.entity.mapper")
public class MybatisPlusMetaObjConfig {

	@Bean("mybatisSqlSession")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, ResourceLoader resourceLoader, GlobalConfiguration globalConfiguration) throws Exception {
		MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(dataSource);
		// sqlSessionFactory.setConfigLocation(resourceLoader.getResource("classpath:mybatis-config.xml"));
		sqlSessionFactory.setTypeAliasesPackage("com.wshsoft.mybatis.test.h2.entity.persistent");
		MybatisConfiguration configuration = new MybatisConfiguration();
		configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
		configuration.setJdbcTypeForNull(JdbcType.NULL);
		sqlSessionFactory.setConfiguration(configuration);
		PaginationInterceptor pagination = new PaginationInterceptor();
        sqlSessionFactory.setPlugins(new Interceptor[]{
                pagination,
                new PerformanceInterceptor()
        });
		sqlSessionFactory.setGlobalConfig(globalConfiguration);
		return sqlSessionFactory.getObject();
	}

	@Bean
	public GlobalConfiguration globalConfiguration() {
		GlobalConfiguration globalConfiguration = new GlobalConfiguration();
		globalConfiguration.setIdType(2);
		globalConfiguration.setMetaObjectHandler(new H2MetaObjectHandler());
		return globalConfiguration;
	}
}
