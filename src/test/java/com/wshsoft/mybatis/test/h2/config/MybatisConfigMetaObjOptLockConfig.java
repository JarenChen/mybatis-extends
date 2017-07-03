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
import com.wshsoft.mybatis.mapper.LogicSqlInjector;
import com.wshsoft.mybatis.plugins.OptimisticLockerInterceptor;
import com.wshsoft.mybatis.plugins.PaginationInterceptor;
import com.wshsoft.mybatis.plugins.PerformanceInterceptor;
import com.wshsoft.mybatis.spring.MybatisSqlSessionFactoryBean;
import com.wshsoft.mybatis.test.h2.H2MetaObjectHandler;

/**
 * <p>
 * MetaObj
 * </p>
 *
 * @author Carry xie
 * @date 2017/6/29
 */
@Configuration
@MapperScan("com.wshsoft.mybatis.test.h2.entity.mapper")
public class MybatisConfigMetaObjOptLockConfig {

    @Bean("mybatisSqlSession")
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, ResourceLoader resourceLoader, GlobalConfiguration globalConfiguration) throws Exception {
        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
//        sqlSessionFactory.setConfigLocation(resourceLoader.getResource("classpath:mybatis-config.xml"));
        sqlSessionFactory.setTypeAliasesPackage("com.wshsoft.mybatis.test.h2.entity.persistent");
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        sqlSessionFactory.setConfiguration(configuration);
        PaginationInterceptor pagination = new PaginationInterceptor();
        sqlSessionFactory.setPlugins(new Interceptor[]{
                pagination,
                new PerformanceInterceptor(),
                new OptimisticLockerInterceptor()
        });
        sqlSessionFactory.setGlobalConfig(globalConfiguration);
        return sqlSessionFactory.getObject();
    }

    @Bean
    public GlobalConfiguration globalConfiguration() {
        GlobalConfiguration conf = new GlobalConfiguration(new LogicSqlInjector());
        conf.setLogicDeleteValue("-1");
        conf.setLogicNotDeleteValue("1");
        conf.setIdType(2);
        conf.setMetaObjectHandler(new H2MetaObjectHandler());
        return conf;
    }
}
