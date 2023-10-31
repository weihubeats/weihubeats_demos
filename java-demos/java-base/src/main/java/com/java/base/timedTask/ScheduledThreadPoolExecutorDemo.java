package com.java.base.timedTask;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *@author : wh
 *@date : 2023/10/31 19:57
 *@description:
 */
public class ScheduledThreadPoolExecutorDemo {


	public static void main(String[] args) {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
		executor.scheduleAtFixedRate(() -> System.out.println("xiao zou time: " + LocalTime.now()), 1000, 2000, TimeUnit.MILLISECONDS); // 1s 延迟后开始执行任务，每 2s 重复执行一次

	}
	
}
