package com.wshsoft.mybatis.test.generator;

import com.wshsoft.mybatis.generator.MybatisPlusGenerator;
import com.wshsoft.mybatis.generator.config.DataSourceConfig;
import com.wshsoft.mybatis.generator.config.PackageConfig;
import com.wshsoft.mybatis.generator.config.StrategyConfig;
import com.wshsoft.mybatis.generator.config.rules.DbType;
import com.wshsoft.mybatis.generator.config.rules.NamingStrategy;

/**
 * <p>
 * 代码生成器演示
 * </p>
 * @author Carry xie
 * @date 2016-11-01
 */
public class MysqlGenerator {

	/**
	 * <p>
	 * MySQL 生成演示
	 * </p>
	 */
	public static void main(String[] args) {
		MybatisPlusGenerator mpg = new MybatisPlusGenerator();
		//输出目录(默认java.io.tmpdir)
		mpg.setOutputDir("D:\\mybatis-extends");
		//是否覆盖同名文件(默认false)
		mpg.setFileOverride(true);
		//是否开启 ActiveRecord 模式(默认true)
		mpg.setActiveRecord(false);
		//mapper.xml 中添加二级缓存配置(默认true)
		mpg.setEnableCache(false);
		//开发者名称 
		mpg.setAuthor("Carry xie");

		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setDbType(DbType.MYSQL);
		dsc.setDriverName("com.mysql.jdbc.Driver");
		dsc.setUsername("root");
		dsc.setPassword("root");
		dsc.setUrl("jdbc:mysql://127.0.0.1:3306/demo?useUnicode=true&amp;useSSL=false");
		mpg.setDataSource(dsc);

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		//表前缀 
		//strategy.setTablePrefix("bmd_");
		//字段生成策略
		strategy.setNaming(NamingStrategy.underline_to_camel);
      
		mpg.setStrategy(strategy);

		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setModuleName("demo");
		mpg.setPackageInfo(pc);

		// 执行生成
		mpg.execute();
	}

}
