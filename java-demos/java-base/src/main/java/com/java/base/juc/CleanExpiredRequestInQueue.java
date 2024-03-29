package com.java.base.juc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author : wh
 * @date : 2024/3/15 16:57
 * @description:
 */
public class CleanExpiredRequestInQueue {
    
    private static final ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(1, new ThreadFactoryImpl("scheduledExecutorService", true));

    public static void main(String[] args) {

        scheduledExecutorService.schedule(() -> {
            testCleanExpiredRequestInQueue(null, 1000L);
        }, 1000, java.util.concurrent.TimeUnit.MILLISECONDS);
        
    }

    public static void testCleanExpiredRequestInQueue(BlockingQueue<Runnable> blockingQueue, Long maxWaitTimeMillsInQueue) {
        while (true) {
            Runnable runnable = blockingQueue.peek();
            if (runnable == null) {
                break;
            }
            if( runnable instanceof RequestTask) {
                RequestTask requestTask = (RequestTask) runnable;
                final long behind = System.currentTimeMillis() - requestTask.getCreateTimestamp();
                if( behind > maxWaitTimeMillsInQueue) {
                    blockingQueue.remove(runnable);
                }
            }
            
        }
       
        

    }
}
