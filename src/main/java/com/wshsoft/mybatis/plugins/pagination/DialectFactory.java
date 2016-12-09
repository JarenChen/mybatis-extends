package com.wshsoft.mybatis.plugins.pagination;

import com.wshsoft.mybatis.enums.DBType;
import com.wshsoft.mybatis.exceptions.MybatisExtendsException;
import com.wshsoft.mybatis.plugins.pagination.dialects.DB2Dialect;
import com.wshsoft.mybatis.plugins.pagination.dialects.H2Dialect;
import com.wshsoft.mybatis.plugins.pagination.dialects.HSQLDialect;
import com.wshsoft.mybatis.plugins.pagination.dialects.MySqlDialect;
import com.wshsoft.mybatis.plugins.pagination.dialects.OracleDialect;
import com.wshsoft.mybatis.plugins.pagination.dialects.PostgreDialect;
import com.wshsoft.mybatis.plugins.pagination.dialects.SQLServer2005Dialect;
import com.wshsoft.mybatis.plugins.pagination.dialects.SQLServerDialect;
import com.wshsoft.mybatis.plugins.pagination.dialects.SQLiteDialect;

/**
 * <p>
 * 分页方言工厂类
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-01-23
 */
public class DialectFactory {

	/**
	 * <p>
	 * 根据数据库类型选择不同分页方言
	 * </p>
	 * 
	 * @param dbtype
	 *            数据库类型
	 * @return
	 * @throws Exception
	 */
	public static IDialect getDialectByDbtype(String dbtype) throws Exception {
		if (DBType.MYSQL.getDb().equalsIgnoreCase(dbtype)) {
			return new MySqlDialect();
		} else if (DBType.ORACLE.getDb().equalsIgnoreCase(dbtype)) {
			return new OracleDialect();
		} else if (DBType.DB2.getDb().equalsIgnoreCase(dbtype)) {
			return new DB2Dialect();
		} else if (DBType.H2.getDb().equalsIgnoreCase(dbtype)) {
			return new H2Dialect();
		} else if (DBType.SQLSERVER.getDb().equalsIgnoreCase(dbtype)) {
			return new SQLServerDialect();
		} else if (DBType.SQLSERVER2005.getDb().equalsIgnoreCase(dbtype)) {
			return new SQLServer2005Dialect();
		} else if (DBType.POSTGRE.getDb().equalsIgnoreCase(dbtype)) {
			return new PostgreDialect();
		} else if (DBType.HSQL.getDb().equalsIgnoreCase(dbtype)) {
			return new HSQLDialect();
		} else if (DBType.SQLITE.getDb().equalsIgnoreCase(dbtype)) {
			return new SQLiteDialect();
		} else {
			throw new MybatisExtendsException("The database is not supported！dbtype:" + dbtype);
		}
	}

}
