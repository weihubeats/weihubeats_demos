package com.java.base.juc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 *@author : wh
 *@date : 2023/10/31 17:16
 *@description: @see{https://weihubeats.blog.csdn.net/article/details/119978812}
 */
public class CompletableFutureDemo {

	public static void main(String[] args) throws Exception{

		CompletableFuture<Integer> supplyAsync = CompletableFuture.supplyAsync(() -> {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return 1;
		});
		CompletableFuture.runAsync(() -> {
			System.out.println("提交异步任务");
		});
		System.out.println("获取异步任务的结果:" + supplyAsync.get());

		
	}
}
