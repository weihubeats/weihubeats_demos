package com.java.base.juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @author : wh
 * @date : 2024/2/2 15:34
 * @description:
 */
public class InheritableThreadLocalTest {

    private static final AtomicInteger ID_SEQ = new AtomicInteger();
    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(5, r -> new Thread(r, "thread-" + ID_SEQ.getAndIncrement()));
    private static Integer i = 0;
    private static final ThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();

    public static void main(String[] args) throws Exception {
        // 模拟接口调用
        IntStream.range(0, 20).forEach(InheritableThreadLocalTest::testInheritableThreadLocal);
        THREAD_POOL.shutdown();
    }

    public static void testInheritableThreadLocal(int s) {
        inheritableThreadLocal.set("小奏技术" + i++);
        Future<?> submit = THREAD_POOL.submit(new ZouTask("任务" + s));
        try {
            submit.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ZouTask implements Runnable {

        String taskName;

        public ZouTask(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public void run() {
            System.out.println(taskName + "线程：" + Thread.currentThread().getName() + "获取到的值: " + inheritableThreadLocal.get());
            inheritableThreadLocal.remove();
        }

    }

    
}
