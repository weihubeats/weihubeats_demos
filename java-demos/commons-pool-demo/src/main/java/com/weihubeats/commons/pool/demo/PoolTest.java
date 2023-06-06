package com.weihubeats.commons.pool.demo;

import com.alibaba.fastjson2.JSONObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author : wh
 * @date : 2023/6/5 14:14
 * @description:
 */
public class PoolTest {

    public static void main(String[] args) throws Exception {
//        singleTest();
        threadTest();
    }

    public static void singleTest() throws Exception {
        XiaZouPool myObjectPool = XiaZouPool.INSTANCE;

        numActiveAndNumIdle(myObjectPool);
        TimeUnit.SECONDS.sleep(1);

        XiaoZou obj = myObjectPool.borrowObject();
        System.out.println("ThreadName:" + Thread.currentThread().getName() + " borrowed: " + JSONObject.toJSONString(obj));
        TimeUnit.SECONDS.sleep(1);

        numActiveAndNumIdle(myObjectPool);
        TimeUnit.SECONDS.sleep(1);

        myObjectPool.returnObject(obj);
        System.out.println("ThreadName:" + Thread.currentThread().getName() + " returned: " + JSONObject.toJSONString(obj));
        TimeUnit.SECONDS.sleep(1);

        numActiveAndNumIdle(myObjectPool);

    }

    private static void numActiveAndNumIdle(XiaZouPool myObjectPool) throws Exception {
        int numActive = myObjectPool.getNumActive();
        int numIdle = myObjectPool.getNumIdle();
        System.out.println("ThreadName:" + Thread.currentThread().getName() + " numActive:" + numActive + " numIdle:" + numIdle);
    }

    public static void threadTest() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(9);

        for (int i = 0; i < 20; i++) {
            executorService.submit(() -> {
                try {
                    singleTest();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
    }

}
