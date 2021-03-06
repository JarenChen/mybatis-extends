package com.wshsoft.mybatis.test.h2.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * H2 Sequence test config
 * </p>
 *
 * @author Carry xie
 * @date 2017/6/26
 */
@Configuration
@Import(value = { DBConfig.class, MybatisPlusSequenceConfig.class })
@ComponentScan("com.wshsoft.mybatis.test.h2.service")
public class ServiceSequenceConfig {

}
