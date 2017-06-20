package com.wshsoft.mybatis.test.generator;

import com.wshsoft.mybatis.generator.AutoGenerator;
import com.wshsoft.mybatis.generator.InjectionConfig;
import com.wshsoft.mybatis.generator.config.*;
import com.wshsoft.mybatis.generator.config.converts.MySqlTypeConvert;
import com.wshsoft.mybatis.generator.config.po.TableInfo;
import com.wshsoft.mybatis.generator.config.rules.DbColumnType;
import com.wshsoft.mybatis.generator.config.rules.DbType;
import com.wshsoft.mybatis.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 代码生成器演示
 * </p>
 * 
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
		AutoGenerator mpg = new AutoGenerator();

		// 全局配置
		GlobalConfig gc = new GlobalConfig();
		gc.setOutputDir("D://mybatis-extends");//输出目录
		gc.setFileOverride(true);// 是否覆盖文件
		gc.setActiveRecord(true);// 开启 activeRecord 模式
		gc.setEnableCache(false);// XML 二级缓存
		gc.setBaseResultMap(true);// XML ResultMap
		gc.setBaseColumnList(true);// XML columList
		gc.setAuthor("Carry xie");

		// 自定义文件命名，注意 %s 会自动填充表实体属性！
		// gc.setMapperName("%sMapper");
		// gc.setXmlName("%sMapper");
		// gc.setServiceName("%sService");
		// gc.setServiceImplName("%sServiceImpl");
		// gc.setControllerName("%sAction");
		mpg.setGlobalConfig(gc);

		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setDbType(DbType.MYSQL);// 数据库类型
		// dsc.setTypeConvert(new MyFieldTypeConvert());
		dsc.setTypeConvert(new MySqlTypeConvert() {
			// 自定义数据库表字段类型转换【可选】
			@Override
			public DbColumnType processTypeConvert(String fieldType) {
				System.out.println("转换类型：" + fieldType);
				return super.processTypeConvert(fieldType);
			}
		});
		dsc.setDriverName("com.mysql.jdbc.Driver");
		dsc.setUsername("root");
		dsc.setPassword("root");
		dsc.setUrl("jdbc:mysql://localhost:3306/mybatis-extends?useUnicode=true&amp;useSSL=false");
		mpg.setDataSource(dsc);

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		// strategy.setCapitalMode(true);// 全局大写命名
		// strategy.setDbColumnUnderline(true);//全局下划线命名
		// 表前缀
		strategy.setTablePrefix(new String[] { "bmd_", "mp_" });// 此处可以修改为您的表前缀
		// 表名生成策略
		strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
		// strategy.setInclude(new String[] { "user" }); // 需要生成的表
		// strategy.setExclude(new String[]{"test"}); // 排除生成的表
		// 自定义实体父类
		// strategy.setSuperEntityClass("com.wshsoft.demo.TestEntity");
		// 自定义实体，公共字段
		// strategy.setSuperEntityColumns(new String[] { "test_id", "age" });
		// 自定义 mapper 父类
		// strategy.setSuperMapperClass("com.wshsoft.demo.TestMapper");
		// 自定义 service 父类
		// strategy.setSuperServiceClass("com.wshsoft.demo.TestService");
		// 自定义 service 实现类父类
		// strategy.setSuperServiceImplClass("com.wshsoft.demo.TestServiceImpl");
		// 自定义 controller 父类
		// strategy.setSuperControllerClass("com.wshsoft.demo.TestController");
		// 【实体】是否生成字段常量（默认 false）
		// public static final String ID = "test_id";
		// strategy.setEntityColumnConstant(true);
		// 【实体】是否为构建者模型（默认 false）
		// public User setName(String name) {this.name = name; return this;}
		// strategy.setEntityBuliderModel(true);
	
        // 【实体】是否为lombok模型（默认 false）<a href="https://projectlombok.org/">document</a>
        // strategy.setEntityLombokModel(true)
        // Boolean类型字段是否移除is前缀处理
        // strategy.setEntityBooleanColumnRemoveIsPrefix(true)
        // strategy.setRestControllerStyle(true)
        // strategy.setControllerMappingHyphenStyle(true)
	mpg.setStrategy(strategy);
		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setModuleName("demo");
		pc.setParent("com.wshsoft");// 自定义包路径
		pc.setController("controller");// 这里是控制器包名，默认 web
		mpg.setPackageInfo(pc);

		// 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
		InjectionConfig cfg = new InjectionConfig() {
			@Override
			public void initMap() {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
				this.setMap(map);
			}
		};
		// 自定义文件名称 --start
		/*
		 * List<FileOutConfig> focList = new ArrayList<>(); focList.add(new
		 * FileOutConfig("/templates/mapper.xml.vm") {
		 * 
		 * @Override public String outputFile(TableInfo tableInfo) { return
		 * "D://mybatis-extends/xml/" + tableInfo.getEntityName() + ".xml"; }
		 * }); cfg.setFileOutConfigList(focList);
		 */
		mpg.setCfg(cfg);

		// 关闭默认 xml 生成，调整生成 至 根目录 --start
		/*
		 * TemplateConfig tc = new TemplateConfig();
		 * tc.setXml(null);
		 * mpg.setTemplate(tc);
		 */
		// 自定义模板配置，模板可以参考源码 /mybatis-extends/src/main/resources/template 使用 copy
		// 至您项目 src/main/resources/template 目录下，模板名称也可自定义如下配置：
		// TemplateConfig tc = new TemplateConfig();
		// tc.setController("...");
		// tc.setEntity("...");
		// tc.setMapper("...");
		// tc.setXml("...");
		// tc.setService("...");
		// tc.setServiceImpl("...");
		// mpg.setTemplate(tc);

		// 执行生成
		mpg.execute();

        // 打印注入设置，这里演示模板里面怎么获取注入内容【可无】
        System.err.println(mpg.getCfg().getMap().get("abc"));
    }

}
