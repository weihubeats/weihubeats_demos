package com.java.base.juc;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : wh
 * @date : 2024/3/15 17:25
 * @description:
 */
public class ThreadFactoryImpl implements ThreadFactory {
    
    private final String threadNamePrefix;

    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    
    private final boolean daemon;

    public ThreadFactoryImpl(String threadNamePrefix) {
        this(threadNamePrefix, false);
    }

    public ThreadFactoryImpl(String threadNamePrefix, boolean daemon) {
        this.threadNamePrefix = threadNamePrefix;
        this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, threadNamePrefix + atomicInteger.incrementAndGet());
        thread.setDaemon(daemon);
        thread.setUncaughtExceptionHandler((t, e) -> {
            System.out.println("Thread has an uncaught exception, threadId=" + t.getId() + ", threadName=" + t.getName());
        });
        return thread;
    }

   
}
