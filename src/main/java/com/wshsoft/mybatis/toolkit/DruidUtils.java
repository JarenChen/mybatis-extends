package com.wshsoft.mybatis.toolkit;

import com.alibaba.druid.sql.PagerUtils;

/**
 * <p>
 * DruidUtils工具类
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-11-30
 */
public class DruidUtils {

    /**
     * 通过Druid方式获取count语句
     * 
     * @param originalSql
     * @param dialectType
     * @return
     */
    public static String count(String originalSql, String dialectType) {
        return PagerUtils.count(originalSql, dialectType);
    }

}
