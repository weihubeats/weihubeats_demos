package com.java.base.limter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *@author : wh
 *@date : 2023/10/26 19:52
 *@description: 固定窗口算法又叫计数器算法，是一种简单方便的限流算法。主要通过一个支持原子操作的计数器来累计 1 秒内的请求次数，当 1 秒内计数达到限流阈值时触发拒绝策略。每过 1 秒，计数器重置为 0 开始重新计数
 */
public class FixedWindowRateLimiter {

	/**
	 * 请求次数限制
	 */
	private final int limit;

	/**
	 * 窗口大小(毫秒)
	 */
	private final long windowSizeInMillis;
	
	/**
	 * 请求记录
	 */
	private final ConcurrentHashMap<Long, AtomicInteger> requestRecord;

	public FixedWindowRateLimiter(int limit, long windowSize, TimeUnit timeUnit) {
		this.limit = limit;
		this.windowSizeInMillis = timeUnit.toMillis(windowSize);
		this.requestRecord = new ConcurrentHashMap<>();
	}

	public boolean allowRequest() {
		long currentTime = System.currentTimeMillis();
		long windowStart = currentTime - windowSizeInMillis;
		// 清理过期的请求记录
		requestRecord.entrySet().removeIf(entry -> entry.getKey() < windowStart);
		AtomicInteger requestCount = requestRecord.computeIfAbsent(currentTime, k -> new AtomicInteger(0));
		int updatedCount = requestCount.incrementAndGet();
		return updatedCount <= limit;
	}

	public static void main(String[] args) throws Exception{
		FixedWindowRateLimiter fixedWindowRateLimiter = new FixedWindowRateLimiter(5, 1, TimeUnit.SECONDS);

		ExecutorService executor = Executors.newFixedThreadPool(10);
		// 模拟并发请求
		for (int i = 1; i <= 20; i++) {
			int finalI = i;
			executor.execute(() -> {
				if (fixedWindowRateLimiter.allowRequest()) {
					System.out.println("处理请求 " + finalI);
				} else {
					System.out.println("超过请求限制，无法处理请求 " + finalI);
				}
			});
		}

		// 关闭线程池
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.MINUTES);
		
	}



}
