package com.java.base.timedTask;

import java.util.Timer;
import java.util.TimerTask;

/**
 *@author : wh
 *@date : 2023/10/31 17:53
 *@description:
 * 1. Timer 是单线程模式。如果某个 TimerTask 执行时间很久，会影响其他任务的调度
 * 2. Timer 的任务调度是基于系统绝对时间的，如果系统时间不正确，可能会出现问题
 * 3. TimerTask 如果执行出现异常，Timer 并不会捕获，会导致线程终止，其他任务永远不会执行
 */
public class TimerDemo {

	public static void main(String[] args) {
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				// do something
				System.out.println("xiao zou timer ");
			}
		}, 2000, 1000);  // 2s 后调度 周期为 1s
	}
}
