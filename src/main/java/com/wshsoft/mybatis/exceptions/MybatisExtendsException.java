package com.wshsoft.mybatis.exceptions;

/**
 * <p>
 * MybatisExtendsException 异常类
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-01-23
 */
public class MybatisExtendsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MybatisExtendsException(String message) {
        super(message);
    }

    public MybatisExtendsException(Throwable throwable) {
        super(throwable);
    }

    public MybatisExtendsException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
