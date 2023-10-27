package com.java.base.limter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 *@author : wh
 *@date : 2023/10/26 19:52
 *@description:
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
	 * 当前时间窗口内的请求数量
	 */
	private final LongAdder count;
	
	/**
	 * 请求记录
	 */
	private final ConcurrentHashMap<Long, AtomicInteger> requestRecord;

	public FixedWindowRateLimiter(int limit, long windowSize, TimeUnit timeUnit) {
		this.limit = limit;
		this.windowSizeInMillis = timeUnit.toMillis(windowSize);
		this.count = new LongAdder();
		this.requestRecord = new ConcurrentHashMap<>();
	}

	public boolean allowRequest() {
		long currentTime = System.currentTimeMillis();
		long windowStart = currentTime - windowSizeInMillis;
		// 清理过期的请求记录
		requestRecord.entrySet().removeIf(entry -> entry.getKey() < windowStart);
		// 检查请求数量是否超过限制
		if (count.intValue() < limit) {
			// 增加当前时间的请求数量
			AtomicInteger requestCount = requestRecord.computeIfAbsent(currentTime, k -> new AtomicInteger(0));
			int updatedCount = requestCount.incrementAndGet();
			// 增加总请求数量
			count.increment();
			return updatedCount <= limit;
		} else {
			return false;
		}
	}

	public static void main(String[] args) {
		
	}


}
