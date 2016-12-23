package com.wshsoft.mybatis.enums;

/**
 * <p>
 * Mybatis Plus 全局注入 规则
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-12-22
 */
public enum InjectionRules {
	REQUIREDPK(1, "需要主键"), UNREQUIREDPK(2, "不需要主键");

	private final int type;

	/** 描述 */
	private final String desc;

	InjectionRules(final int type, final String desc) {
		this.type = type;
		this.desc = desc;
	}

	public static InjectionRules getInjectionRule(int type) {
		InjectionRules[] injectionRules = InjectionRules.values();
		for (InjectionRules rules : injectionRules) {
			if (rules.getType() == type) {
				return rules;
			}
		}
		return REQUIREDPK;
	}

	public int getType() {
		return this.type;
	}

	public String getDesc() {
		return this.desc;
	}

}
