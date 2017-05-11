package com.wshsoft.mybatis.test;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import com.wshsoft.mybatis.toolkit.IdWorker;

/**
 * <p>
 * IdWorker 并发测试
 * </p>
 * 
 * @author Carry xie
 * @date 2016-08-01
 */
public class IdWorkerTest {

	@Test
	public void test() throws Exception {
		int count = 1000;
		ExecutorService executorService = Executors.newFixedThreadPool(20);
		final List<Long> results = new ArrayList<>();
		CompletionService<Long> cs = new ExecutorCompletionService<Long>(executorService);

		for (int i = 1; i < count; i++) {
			cs.submit(new Callable<Long>() {
				public Long call() throws Exception {
					return IdWorker.getId();
				}
			});
		}
		for (int i = 0; i < count; i++) {
			Future<Long> future = executorService.submit(new Callable<Long>() {
				@Override
				public Long call() throws Exception {
					return IdWorker.getId();
				}
			});
			results.add(future.get());
		}
		executorService.shutdown();
		int odd = 0;
		int even = 0;
		List<Long> ttt = new ArrayList<>();
		for (Long id : results) {

			if (ttt.contains(id)) {
				System.err.println("ssss");
			}
			ttt.add(id);
			if (id % 2 != 0) {
				odd++;
			} else {
				even++;
			}
		}
		System.err.println("奇数:" + odd);
		System.err.println("偶数:" + even);
		Assert.assertTrue(odd >= 450 && odd <= 550);
		Assert.assertTrue(even >= 450 && even <= 550);
	}

}
