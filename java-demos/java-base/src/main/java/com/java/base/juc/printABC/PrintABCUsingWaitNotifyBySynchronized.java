package com.java.base.juc.printABC;

/**
 * @author : wh
 * @date : 2024/1/20 10:49
 * @description:
 */
public class PrintABCUsingWaitNotifyBySynchronized {
    
    
    public static void main(String[] args) {
        PrintABCUsingWaitNotifyBySynchronized printABCUsingWaitNotify = new PrintABCUsingWaitNotifyBySynchronized();
        new Thread(() -> printABCUsingWaitNotify.print("A", 1, 2)).start();
        new Thread(() -> printABCUsingWaitNotify.print("B", 2, 3)).start();
        new Thread(() -> printABCUsingWaitNotify.print("C", 3, 1)).start();
    }
    
    private void print(String name, int waitFlag, int nextFlag) {
        int count = 10;
        for (int i = 0; i < count; i++) {
            synchronized (this) {
                while (flag != waitFlag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(Thread.currentThread().getName() + "-" + name);
                flag = nextFlag;
                this.notifyAll();
            }
        }
    }
    
    private volatile int flag = 1;
    
}
