package com.java.base.timedTask;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : wh
 * @date : 2024/7/12 17:28
 * @description:
 */
public class HashedWheelTimer {

    private final ExecutorService executor;

    /**
     * 时间槽
     */
    private final HashedWheelBucket[] wheel;

    /**
     * 时间槽的时间跨度
     */
    private final long tickDuration;

    /**
     * 任务数量
     */
    private final AtomicInteger taskCounter = new AtomicInteger(0);

    private final int mask;

    private long currentTick = 0;

    public HashedWheelTimer(int wheelSize, long tickDuration) {
        this.wheel = new HashedWheelBucket[wheelSize];
        this.tickDuration = tickDuration;
        this.mask = wheelSize - 1;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i < wheelSize; i++) {
            wheel[i] = new HashedWheelBucket();
        }

        Thread tickThread = new Thread(this::run);
        tickThread.setDaemon(true);
        tickThread.start();
    }

    public ScheduledFuture<?> schedule(Runnable task, long delay, TimeUnit unit) {
        long delayMs = unit.toMillis(delay);
        long deadline = System.currentTimeMillis() + delayMs;
        long ticks = delayMs / tickDuration;
        int stopIndex = (int) ((currentTick + ticks) & mask);

        HashedWheelTimerFuture future = new HashedWheelTimerFuture(task, deadline);
        wheel[stopIndex].addTask(future);
        taskCounter.incrementAndGet();
        return future;
    }

    private void run() {
        while (!Thread.currentThread().isInterrupted()) {
            long deadline = System.currentTimeMillis() + tickDuration;
            int bucket = (int) (currentTick & mask);
            wheel[bucket].expireTimerTasks(currentTick);
            currentTick++;

            long sleepTime = deadline - System.currentTimeMillis();
            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }

    private class HashedWheelBucket {
        private final ConcurrentLinkedQueue<HashedWheelTimerFuture> tasks = new ConcurrentLinkedQueue<>();

        public void addTask(HashedWheelTimerFuture task) {
            tasks.offer(task);
        }

        public void expireTimerTasks(long currentTick) {
            long currentTimeMillis = System.currentTimeMillis();
            HashedWheelTimerFuture task;
            while ((task = tasks.peek()) != null) {
                if (task.deadline <= currentTimeMillis) {
                    tasks.poll();
                    if (!task.isCancelled()) {
                        executor.execute(task.task);
                    }
                    taskCounter.decrementAndGet();
                } else {
                    break;
                }
            }
        }
    }

    private static class HashedWheelTimerFuture implements ScheduledFuture<Void> {

        private final Runnable task;

        private final long deadline;

        private volatile boolean cancelled = false;

        public HashedWheelTimerFuture(Runnable task, long deadline) {
            this.task = task;
            this.deadline = deadline;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(deadline - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(getDelay(TimeUnit.MILLISECONDS), o.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            cancelled = true;
            return true;
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public boolean isDone() {
            return cancelled || System.currentTimeMillis() >= deadline;
        }

        @Override
        public Void get() {
            return null;
        }

        @Override
        public Void get(long timeout, TimeUnit unit) {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM dd:HH:mm:ss");
        HashedWheelTimer timer = new HashedWheelTimer(512, 100); // 512个槽，每个tick 100毫秒
        System.out.println("taks start time: " + LocalDateTime.now().format(formatter));
        timer.schedule(() -> System.out.println("Task executed1, time: " + LocalDateTime.now().format(formatter)), 1, TimeUnit.SECONDS);
        timer.schedule(() -> System.out.println("Task executed2, time: " + LocalDateTime.now().format(formatter)), 3, TimeUnit.SECONDS);
        timer.schedule(() -> System.out.println("Task executed3, time: " + LocalDateTime.now().format(formatter)), 5, TimeUnit.SECONDS);
        timer.schedule(() -> System.out.println("Task executed4, time: " + LocalDateTime.now().format(formatter)), 7, TimeUnit.SECONDS);
        TimeUnit.SECONDS.sleep(20);
    }
}
