package com.wshsoft.mybatis.incrementer;

/**
 * <p>
 * Oracle Key Sequence 生成器
 * </p>
 *
 * @author Carry xie
 * @Date 2017-05-08
 */
public class OracleKeyGenerator implements IKeyGenerator {

    @Override
    public String executeSql(String incrementerName) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(incrementerName);
        sql.append(".NEXTVAL FROM DUAL");
        return sql.toString();
    }
}
