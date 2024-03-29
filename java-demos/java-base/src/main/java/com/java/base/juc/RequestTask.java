package com.java.base.juc;

/**
 * @author : wh
 * @date : 2024/3/15 17:17
 * @description:
 */
public class RequestTask implements Runnable {
    
    private Long createTimestamp = System.currentTimeMillis();
    
    private final Runnable runnable;

    public RequestTask(Long createTimestamp, Runnable runnable) {
        this.createTimestamp = createTimestamp;
        this.runnable = runnable;
    }

    @Override
    public void run() {
        this.runnable.run();
    }

    public Long getCreateTimestamp() {
        return createTimestamp;
    }

    public Runnable getRunnable() {
        return runnable;
    }
}
