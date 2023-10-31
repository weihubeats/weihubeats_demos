package com.java.base.limter;

import java.time.LocalTime;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *@author : wh
 *@date : 2023/10/31 15:11
 *@description:
 */
public class SlidingWindowRateLimiter {

	/**
	 * 请求限制数量
	 */
	private final int limit;

	/**
	 * 滑动窗口窗口大小(毫秒)
	 */
	private final  long windowSizeInMillis;

	/**
	 * 请求队列
	 */
	private final ConcurrentLinkedQueue<Long> requestQueue;

	/**
	 * 当前窗口的请求数量
	 */
	private final AtomicInteger count;

	public SlidingWindowRateLimiter(int limit, long windowSize, TimeUnit timeUnit) {
		this.limit = limit;
		this.windowSizeInMillis = timeUnit.toMillis(windowSize);
		this.requestQueue = new ConcurrentLinkedQueue<>();
		this.count = new AtomicInteger(0);
	}

	public synchronized boolean allowRequest() {
		long currentTime = System.currentTimeMillis();
		long windowStart = currentTime - windowSizeInMillis;
		// 清理过期请求
		while (!requestQueue.isEmpty() && requestQueue.peek() < windowStart) {
			requestQueue.poll();
			count.decrementAndGet();
		}

		if (count.incrementAndGet() <= limit) {
			requestQueue.offer(currentTime);
			return true;
		}
		else {
			count.decrementAndGet();
			return false;
		} 
		
	}

	public static void main(String[] args) throws Exception{
		
		SlidingWindowRateLimiter fixedWindowRateLimiter = new SlidingWindowRateLimiter(5, 1, TimeUnit.SECONDS);

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
		TimeUnit.SECONDS.sleep(1);
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
