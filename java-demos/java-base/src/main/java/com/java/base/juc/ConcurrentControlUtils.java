package com.java.base.juc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author : wh
 * @date : 2023/3/17 10:07
 * @description:
 */
public class ConcurrentControlUtils {

    /**
     * 默认并发数
     */
    private static final int DEFAUlT_CONCURRENT_THREAD_NUM = 200;

    public static void process(Runnable runnable) {
        process(runnable, DEFAUlT_CONCURRENT_THREAD_NUM);
    }

    /**
     *  执行器
     * @param runnable 任务
     * @param concurrentThreadNum 并发数
     */
    public static void process(Runnable runnable, int concurrentThreadNum) {

        /**
         * 并发数
         */
        if (concurrentThreadNum <= 0) {
            concurrentThreadNum = 200;
        }

        CountDownLatch blockLatch = new CountDownLatch(concurrentThreadNum);
        ExecutorService threadPool = Executors.newFixedThreadPool(concurrentThreadNum);

        for (int i = 0; i < concurrentThreadNum; i++) {
            threadPool.submit(() -> {
                try {
                    blockLatch.await();
                    runnable.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            });
            if (i == concurrentThreadNum - 1) {
                System.out.println("并发测试开始");
            }
            blockLatch.countDown();

        }


        blockingMainThread(threadPool);



    }

    private static void blockingMainThread(ExecutorService threadPool) {
        threadPool.shutdown();
        while (!threadPool.isTerminated()) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("线程池关闭");
    }
}
