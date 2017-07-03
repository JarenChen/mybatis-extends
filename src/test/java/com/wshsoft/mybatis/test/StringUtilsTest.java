package com.wshsoft.mybatis.test;

import org.junit.Assert;
import org.junit.Test;

import com.wshsoft.mybatis.toolkit.StringUtils;

/**
 * <p>
 * 字符串工具类测试
 * </p>
 *
 * @author Carry xie
 * @date 2017-06-27
 */
public class StringUtilsTest {

    @Test
    public void removePrefixAfterPrefixToLower(){
        Assert.assertEquals("user", StringUtils.removePrefixAfterPrefixToLower( "isUser", 2 ));
        Assert.assertEquals("userInfo", StringUtils.removePrefixAfterPrefixToLower( "isUserInfo", 2 ));
    }
}
