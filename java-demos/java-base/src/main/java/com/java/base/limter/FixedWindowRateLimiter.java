package com.java.base.limter;

import java.time.LocalTime;
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
	 * 开始时间
	 */
	private  static  long START_TIME = System.currentTimeMillis();

	/**
	 * 计数器 可以使用原子类，方法已经添加了 synchronized
	 */
	private static final AtomicInteger REQCOUNT = new AtomicInteger();
	

	public FixedWindowRateLimiter(int limit, long windowSize, TimeUnit timeUnit) {
		this.limit = limit;
		this.windowSizeInMillis = timeUnit.toMillis(windowSize);
	}

	public synchronized boolean allowRequest() {
		if ((System.currentTimeMillis() - START_TIME) > windowSizeInMillis) {
			REQCOUNT.set(0);
			START_TIME = System.currentTimeMillis();
		}
		return REQCOUNT.incrementAndGet() <= limit;
	}

	public static void main(String[] args) throws Exception{
		FixedWindowRateLimiter fixedWindowRateLimiter = new FixedWindowRateLimiter(5, 1, TimeUnit.SECONDS);

		ExecutorService executor = Executors.newFixedThreadPool(10);
		// 模拟并发请求
		for (int i = 1; i <= 20; i++) {
			int finalI = i;
			executor.execute(() -> {
				if (fixedWindowRateLimiter.allowRequest()) {
					LocalTime now = LocalTime.now();
					System.out.println(now + " 处理请求 " + finalI);

				} else {
					LocalTime now = LocalTime.now();
					System.out.println(now + " 超过请求限制，无法处理请求 " + finalI);
				}
			});
		}

		// 关闭线程池
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.MINUTES);
		
	}



}
