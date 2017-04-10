package com.wshsoft.mybatis.test;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;

import com.wshsoft.mybatis.toolkit.IdWorker;

/**
 * <p>
 * 并发测试
 * </p>
 * 
 * @author Carry xie
 * @date 2016-08-18
 */
public class ContiPerfTest {

	@Rule
	public ContiPerfRule i = new ContiPerfRule();

	/**
	 * samples: 2000000 max: 82 average: 0.0039698 median: 0
	 */
	@Test
	@PerfTest(invocations = 2000000, threads = 16)
	public void testIdWorker() throws Exception {
		IdWorker.getId();
	}

}
