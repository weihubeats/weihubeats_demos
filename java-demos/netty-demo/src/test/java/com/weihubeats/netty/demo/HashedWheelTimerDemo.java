package com.weihubeats.netty.demo;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.Timer;

/**
 *@author : wh
 *@date : 2023/11/1 17:47
 *@description:
 */
public class HashedWheelTimerDemo {

	public static void main(String[] args) {

		Timer timer = new HashedWheelTimer();
		Timeout timeout1 = timer.newTimeout(timeout -> System.out.println("timeout1: " + LocalDateTime.now()), 10, TimeUnit.SECONDS);
		if (!timeout1.isExpired()) {
			timeout1.cancel();
		}
		timer.newTimeout(timeout -> {
			System.out.println("timeout2: " + new Date());
			Thread.sleep(5000);
		}, 1, TimeUnit.SECONDS);
		timer.newTimeout(timeout -> System.out.println("timeout3: " + LocalDateTime.now()), 3, TimeUnit.SECONDS);

	}
	
}
