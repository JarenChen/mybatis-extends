package com.wshsoft.mybatis.test.ioc;

/**
 * <p>
 * 试驾人员
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-07-06
 */
public class Human {

	private String name;

	public Human(String name) {
		this.name = name;
	}

	public void driver(ICar car) {
		System.out.println("\n" + name + " 开始！试驾... ");
		if (car.start()) {
			car.driver();
			System.out.println(" 试驾结束！ ");
		} else {
			System.out.println(" 熄火！ ");
		}
	}

}
