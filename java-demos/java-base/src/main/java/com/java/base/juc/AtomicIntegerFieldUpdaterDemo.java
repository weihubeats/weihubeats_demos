package com.java.base.juc;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 *@author : wh
 *@date : 2023/7/21 11:25
 *@description:
 */
public class AtomicIntegerFieldUpdaterDemo {

	private volatile int value;

	private static final AtomicIntegerFieldUpdater<AtomicIntegerFieldUpdaterDemo> atomicLongFieldUpdater =
			AtomicIntegerFieldUpdater.newUpdater(AtomicIntegerFieldUpdaterDemo.class, "value");

	public static void main(String[] args) {
		AtomicIntegerFieldUpdaterDemo updaterDemo = new AtomicIntegerFieldUpdaterDemo();
		updaterDemo.value = 20;

		// 使用 AtomicIntegerFieldUpdater 更新 value1 字段的值
		boolean b = atomicLongFieldUpdater.compareAndSet(updaterDemo, 20, 30);
		System.out.println("value " + updaterDemo.value);
		

	}
}
