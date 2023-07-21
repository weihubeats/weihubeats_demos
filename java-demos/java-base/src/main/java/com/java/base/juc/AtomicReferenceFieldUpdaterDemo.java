package com.java.base.juc;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 *@author : wh
 *@date : 2023/7/21 11:31
 *@description:
 */
public class AtomicReferenceFieldUpdaterDemo {
	
	private volatile String value;

	private static final AtomicReferenceFieldUpdater<AtomicReferenceFieldUpdaterDemo, String> UPDATER = AtomicReferenceFieldUpdater.newUpdater(AtomicReferenceFieldUpdaterDemo.class, String.class, "value");

	public static void main(String[] args) {
		AtomicReferenceFieldUpdaterDemo demo = new AtomicReferenceFieldUpdaterDemo();
		demo.value = "weihubeats";
		UPDATER.compareAndSet(demo, "weihubeats", "weihubeats-update");
	}
	
}
