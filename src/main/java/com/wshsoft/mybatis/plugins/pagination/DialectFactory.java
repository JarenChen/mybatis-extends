package com.wshsoft.mybatis.plugins.pagination;

import org.apache.ibatis.session.RowBounds;

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
import com.wshsoft.mybatis.toolkit.StringUtils;

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
     * 生成翻页执行 SQL
     * </p>
     * 
     * @param page 翻页对象
     * @param buildSql 执行 SQL
     * @param dialectType 方言类型
     * @param dialectClazz 自定义方言实现类
     * @return
     * @throws Exception
     */
    public static String buildPaginationSql(Pagination page, String buildSql, String dialectType, String dialectClazz)
            throws Exception {
        // fix #172, 196
        return getiDialect(dialectType, dialectClazz).buildPaginationSql(buildSql, page.getOffsetCurrent(),
                page.getSize());
    }

    /**
     * Physical Pagination Interceptor for all the queries with parameter
     * {@link org.apache.ibatis.session.RowBounds}
     * 
     * @param rowBounds
     * @param buildSql
     * @param dialectType
     * @param dialectClazz
     * @return
     * @throws Exception
     */
    public static String buildPaginationSql(RowBounds rowBounds, String buildSql, String dialectType,
                                            String dialectClazz) throws Exception {
        // fix #196
        return getiDialect(dialectType, dialectClazz).buildPaginationSql(buildSql, rowBounds.getOffset(),
                rowBounds.getLimit());
    }

    /**
     * <p>
     * 获取数据库方言
     * </p>
     * 
     * @param dialectType 方言类型
     * @param dialectClazz 自定义方言实现类
     * @return
     * @throws Exception
     */
    private static IDialect getiDialect(String dialectType, String dialectClazz) throws Exception {
        IDialect dialect = null;
        if (StringUtils.isNotEmpty(dialectType)) {
            dialect = getDialectByDbtype(dialectType);
        } else {
            if (StringUtils.isNotEmpty(dialectClazz)) {
                try {
                    Class<?> clazz = Class.forName(dialectClazz);
                    if (IDialect.class.isAssignableFrom(clazz)) {
                        dialect = (IDialect) clazz.newInstance();
                    }
                } catch (ClassNotFoundException e) {
                    throw new MybatisExtendsException("Class :" + dialectClazz + " is not found");
                }
            }
        }
        /* 未配置方言则抛出异常 */
        if (dialect == null) {
            throw new MybatisExtendsException(
                    "The value of the dialect property in mybatis configuration.xml is not defined.");
        }
        return dialect;
    }

    /**
     * <p>
     * 根据数据库类型选择不同分页方言
     * </p>
     * 
     * @param dbtype 数据库类型
     * @return
     * @throws Exception
     */
    private static IDialect getDialectByDbtype(String dbtype) throws Exception {
        IDialect dialect = null;
        if (DBType.MYSQL.getDb().equalsIgnoreCase(dbtype)) {
            dialect = MySqlDialect.INSTANCE;
        } else if (DBType.ORACLE.getDb().equalsIgnoreCase(dbtype)) {
            dialect = OracleDialect.INSTANCE;
        } else if (DBType.DB2.getDb().equalsIgnoreCase(dbtype)) {
            dialect = DB2Dialect.INSTANCE;
        } else if (DBType.H2.getDb().equalsIgnoreCase(dbtype)) {
            dialect = H2Dialect.INSTANCE;
        } else if (DBType.SQLSERVER.getDb().equalsIgnoreCase(dbtype)) {
            dialect = SQLServerDialect.INSTANCE;
        } else if (DBType.SQLSERVER2005.getDb().equalsIgnoreCase(dbtype)) {
            dialect = SQLServer2005Dialect.INSTANCE;
        } else if (DBType.POSTGRE.getDb().equalsIgnoreCase(dbtype)) {
            dialect = PostgreDialect.INSTANCE;
        } else if (DBType.HSQL.getDb().equalsIgnoreCase(dbtype)) {
            dialect = HSQLDialect.INSTANCE;
        } else if (DBType.SQLITE.getDb().equalsIgnoreCase(dbtype)) {
            dialect = SQLiteDialect.INSTANCE;
        } else {
            throw new MybatisExtendsException("The database is not supported！dbtype:" + dbtype);
        }
        return dialect;
    }

}
