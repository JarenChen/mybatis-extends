package com.wshsoft.mybatis.plugins;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.wshsoft.mybatis.plugins.pagination.Pagination;

/**
 * <p>
 * 实现分页辅助类
 * </p>
 * 
 * @author Carry xie
 * @Date 2016-03-01
 */
public class Page<T> extends Pagination {

	private static final long serialVersionUID = 1L;

	/**
	 * 查询数据列表
	 */
	private List<T> records = Collections.emptyList();

	/**
	 * 查询参数
	 */
	private Map<String, Object> condition = new ConcurrentHashMap<String, Object>();

	public Page() {
		/* 注意，传入翻页参数 */
	}

	public Page(int current, int size) {
		super(current, size);
	}

	public Page(int current, int size, String orderByField) {
		super(current, size);
		this.setOrderByField(orderByField);
	}

	public List<T> getRecords() {
		return records;
	}

	public Page<T> setRecords(List<T> records) {
		this.records = records;
		return this;
	}

	public Map<String, Object> getCondition() {
		return condition;
	}

	public Page<T> setCondition(Map<String, Object> condition) {
		this.condition = condition;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder pg = new StringBuilder();
		pg.append(" Page:{ [").append(super.toString()).append("], ");
		if (records != null) {
			pg.append("records-size:").append(records.size());
		} else {
			pg.append("records is null");
		}
		return pg.append(" }").toString();
	}

}
