package com.java.base.juc.printABC;

import java.util.concurrent.Semaphore;

/**
 * @author : wh
 * @date : 2024/1/20 10:54
 * @description:
 */
public class PrintABCUsingSemaphore {

    private static final Semaphore s1 = new Semaphore(1);
    
    private static final Semaphore s2 = new Semaphore(0);

    private static final Semaphore s3 = new Semaphore(0);

    public static void main(String[] args) {
        PrintABCUsingSemaphore lock = new PrintABCUsingSemaphore();
        new Thread(() -> lock.printABC(s1, s2, "A")).start();
        new Thread(() -> lock.printABC(s2, s3, "B")).start();
        new Thread(() -> lock.printABC(s3, s1, "C")).start();

    }

    private void printABC(Semaphore currentThread, Semaphore nextTread, String name) {

        for (int i = 0; i < 10; i++) {
            try {
                currentThread.acquire();
                System.out.println(Thread.currentThread().getName() + "-" + name);
                nextTread.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        
    }
    
    
    
    
    
    
}
