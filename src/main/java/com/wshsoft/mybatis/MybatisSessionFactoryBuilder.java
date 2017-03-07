package com.wshsoft.mybatis;

import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.wshsoft.mybatis.entity.GlobalConfiguration;
import com.wshsoft.mybatis.toolkit.IOUtils;

/**
 * <p>
 * replace default SqlSessionFactoryBuilder class
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-01-23
 */
public class MybatisSessionFactoryBuilder extends SqlSessionFactoryBuilder {

    private GlobalConfiguration globalConfig = GlobalConfiguration.defaults();

    @Override
    public SqlSessionFactory build(Reader reader, String environment, Properties properties) {
        try {
            MybatisXMLConfigBuilder parser = new MybatisXMLConfigBuilder(reader, environment, properties);
            return globalConfig.signGlobalConfig(build(parser.parse()));
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error building SqlSession.", e);
        } finally {
            ErrorContext.instance().reset();
            IOUtils.closeQuietly(reader);
        }
    }

    @Override
    public SqlSessionFactory build(InputStream inputStream, String environment, Properties properties) {
        try {
            MybatisXMLConfigBuilder parser = new MybatisXMLConfigBuilder(inputStream, environment, properties);
            return globalConfig.signGlobalConfig(build(parser.parse()));
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error building SqlSession.", e);
        } finally {
            ErrorContext.instance().reset();
            IOUtils.closeQuietly(inputStream);
        }
    }

    // TODO 注入全局配置
    public void setGlobalConfig(GlobalConfiguration globalConfig) {
        this.globalConfig = globalConfig;
    }

}
