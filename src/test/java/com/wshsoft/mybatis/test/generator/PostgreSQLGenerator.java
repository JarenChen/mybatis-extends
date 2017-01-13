package com.wshsoft.mybatis.test.generator;

import java.util.HashMap;
import java.util.Map;

import com.wshsoft.mybatis.generator.AutoGenerator;
import com.wshsoft.mybatis.generator.InjectionConfig;
import com.wshsoft.mybatis.generator.config.DataSourceConfig;
import com.wshsoft.mybatis.generator.config.GlobalConfig;
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
public class PostgreSQLGenerator {

    public static void main(String[] args) {
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        gc.setOutputDir("D://");
        gc.setFileOverride(true);
        gc.setActiveRecord(true);// 开启 activeRecord 模式
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(false);// XML columList
        gc.setAuthor("Carry xie");

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        // gc.setMapperName("%sDao");
        // gc.setXmlName("%sDao");
        // gc.setServiceName("MP%sService");
        // gc.setServiceImplName("%sServiceDiy");
        // gc.setControllerName("%sAction");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.POSTGRE_SQL);
        dsc.setDriverName("org.postgresql.Driver");
        dsc.setUsername("postgres");
        dsc.setPassword("postgres");
        dsc.setUrl("jdbc:postgresql://localhost:5432/mybatis-extends");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setDbColumnUnderline(true);//全局下划线命名
        strategy.setTablePrefix(new String[]{"bmd_", "mp_"});// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.remove_prefix_and_camel);// 表名生成策略
        // strategy.setInclude(new String[] { "user" }); // 需要生成的表
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
        // 字段名生成策略
        strategy.setFieldNaming(NamingStrategy.underline_to_camel);
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
        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName("test");
        pc.setParent("com.wshsoft");//自定义包路径
		pc.setController("controller");//这里是控制器包名，默认 web
        mpg.setPackageInfo(pc);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("abc", this.getConfig().getGlobalConfig().getAuthor());
                this.setMap(map);
            }
        };
        mpg.setCfg(cfg);

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
        // 打印注入设置
        System.err.println(mpg.getCfg().getMap().get("abc"));
    }

}
