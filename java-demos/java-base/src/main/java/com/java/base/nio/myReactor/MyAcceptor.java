package com.java.base.nio.myReactor;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : wh
 * @date : 2024/1/31 19:46
 * @description:
 */
public class MyAcceptor implements Runnable {

    private static final int SERVER_PORT = 8090;


    private static ExecutorService mainReactor = Executors.newSingleThreadExecutor(new ThreadFactory() {

        private AtomicInteger num = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread();
            thread.setName("mainReactor-" + num.incrementAndGet());
            return thread;
        }
    });

    @Override
    public void run() {

        ServerSocketChannel ssc = null;

        try {
            ssc = ServerSocketChannel.open();
            // 设置为非阻塞
            ssc.configureBlocking(false);

            ssc.bind(new InetSocketAddress(SERVER_PORT));
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
