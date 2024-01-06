package com.weihubeats.netty.demo.nio;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : wh
 * @date : 2024/1/4 17:42
 * @description:
 */
public class Acceptor implements Runnable {

    private static ExecutorService mainReactor = Executors.newSingleThreadExecutor(new ThreadFactory() {
        private AtomicInteger num = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("main-reactor-" + num.incrementAndGet());
            return t;
        }
    });


    @Override
    public void run() {

    }

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0L, null, null);
    }
}
