
package com.wshsoft.mybatis.plugins.parser;

import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.reflection.MetaObject;

import com.wshsoft.mybatis.exceptions.MybatisExtendsException;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

/**
 * <p>
 * 抽象 SQL 解析类
 * </p>
 *
 * @author Carry xie
 * @Date 2017-06-20
 */
public abstract class AbstractJsqlParser implements ISqlParser {

    // 日志
    protected final Log logger = LogFactory.getLog(this.getClass());

    /**
     * <p>
     * 获取优化 SQL 方法
     * </p>
     *
     * @param metaObject 元对象
     * @param sql        SQL 语句
     * @return SQL 信息
     */

    @Override
    public SqlInfo optimizeSql(MetaObject metaObject, String sql) {
        if (this.allowProcess(metaObject)) {
            try {
                Statement statement = CCJSqlParserUtil.parse(sql);
                logger.debug("Original SQL: " + sql);
                if (null != statement) {
                    return this.processParser(statement);
                }
            } catch (JSQLParserException e) {
                throw new MybatisExtendsException("Failed to process, please exclude the tableName or statementId.\n Error SQL: " + sql, e);
            }
        }
        return null;
    }

    /**
     * <p>
     * 执行 SQL 解析
     * </p>
     *
     * @param statement JsqlParser Statement
     * @return
     */
    public abstract SqlInfo processParser(Statement statement);

    /**
     * <p>
     * 判断是否允许执行<br>
     * 例如：逻辑删除只解析 delete , update 操作
     * </p>
     *
     * @param metaObject 元对象
     * @return true
     */
    public boolean allowProcess(MetaObject metaObject) {
        return true;
    }
}
