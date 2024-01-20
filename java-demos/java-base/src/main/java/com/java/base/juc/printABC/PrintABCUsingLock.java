package com.java.base.juc.printABC;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : wh
 * @date : 2024/1/20 10:52
 * @description:
 */
public class PrintABCUsingLock {

    private volatile int flag = 1;

    private final Lock lock = new ReentrantLock();

    private final Condition condition = lock.newCondition();
    
        public static void main(String[] args) {
            PrintABCUsingLock printABCUsingLock = new PrintABCUsingLock();
            new Thread(() -> printABCUsingLock.print("A", 1, 2)).start();
            new Thread(() -> printABCUsingLock.print("B", 2, 3)).start();
            new Thread(() -> printABCUsingLock.print("C", 3, 1)).start();
        }
    
        private void print(String name, int waitFlag, int nextFlag) {
            int count = 10;
            for (int i = 0; i < count; i++) {
                lock.lock();
                try {
                    while (flag != waitFlag) {
                        condition.await();
                    }
                    System.out.println(Thread.currentThread().getName() + "-" + name);
                    flag = nextFlag;
                    condition.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    
        
}
