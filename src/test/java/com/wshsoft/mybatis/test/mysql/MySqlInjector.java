package com.wshsoft.mybatis.test.mysql;

import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.session.Configuration;

import com.wshsoft.mybatis.entity.TableInfo;
import com.wshsoft.mybatis.mapper.AutoSqlInjector;

/**
 * <p>
 * 测试自定义注入 SQL
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-07-23
 */
public class MySqlInjector extends AutoSqlInjector {

	@Override
	public void inject(Configuration configuration, MapperBuilderAssistant builderAssistant, Class<?> mapperClass,
			Class<?> modelClass, TableInfo table) {
		/* 添加一个自定义方法 */
		deleteAllUser(mapperClass, modelClass, table);
	}

	public void deleteAllUser(Class<?> mapperClass, Class<?> modelClass, TableInfo table) {

		/* 执行 SQL ，动态 SQL 参考类 SqlMethod */
		String sql = "delete from " + table.getTableName();

		/* mapper 接口方法名一致 */
		String method = "deleteAll";
		SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
		this.addDeleteMappedStatement(mapperClass, method, sqlSource);
	}

}
