package com.java.base.juc;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author : wh
 * @date : 2024/3/28 11:33
 * @description:
 */
public class FirstResponseServiceDemo {

    public static void main(String[] args) {
        CompletableFuture<String> service1 = CompletableFuture.supplyAsync(() -> requestService("order-a"));

        CompletableFuture<String> service2 = CompletableFuture.supplyAsync(() -> requestService("order-b"));

        CompletableFuture<String> service3 = CompletableFuture.supplyAsync(() -> requestService("order-c"));

        CompletableFuture<Object> anyOfFuture = CompletableFuture.anyOf(service1, service2, service3);

        try {
            String o = (String) anyOfFuture.get();
            System.out.println("order Service result: " + o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private static String requestService(String s) {
        System.out.println("requestService: " + s + " start");
        int sleepTime = new Random().nextInt(4);
        try {
            TimeUnit.SECONDS.sleep(sleepTime);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("requestService: " + s + " sleepTime: " + sleepTime + " done");
        return s + ": success";
    }

}
