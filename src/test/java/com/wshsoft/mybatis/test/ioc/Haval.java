package com.wshsoft.mybatis.test.ioc;

/**
 * <p>
 * 哈佛汽车
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-07-06
 */
public class Haval implements ICar {

	@Override
	public boolean start() {
		System.err.println(" 哈佛H9 点火启动... ");
		return true;
	}

	@Override
	public void driver() {
		System.err.println(" 走你！哈佛H9 ... ");
	}

}
