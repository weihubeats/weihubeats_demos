package com.weihubeats.commons.pool.demo;

import com.alibaba.fastjson2.JSONObject;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author : wh
 * @date : 2023/6/7 13:49
 * @description:
 */
public class XiaZouPoolTest {
    
    @Test
    public void singleTest() throws Exception{
        // 最大对象数量3 最小空闲对象数量1 最大空闲数量 2
        XiaZouPool myObjectPool = XiaZouPool.INSTANCE;
        numActiveAndNumIdle(myObjectPool);
        // 获取对象
        XiaoZou obj = myObjectPool.borrowObject();
        // 获取对象
        XiaoZou obj2 = myObjectPool.borrowObject();
        // 获取对象
        XiaoZou obj3 = myObjectPool.borrowObject();
        
        // 获取对象 已超出最大对象数
        XiaoZou obj4 = myObjectPool.borrowObject();
        
        //归还对象
        myObjectPool.returnObject(obj);
        System.out.println("ThreadName:" + Thread.currentThread().getName() + " returned: " + JSONObject.toJSONString(obj));
        sleep();
        numActiveAndNumIdle(myObjectPool);
    }

    /**
     *
     * @param myObjectPool
     */
    private static void numActiveAndNumIdle(XiaZouPool myObjectPool) {
        int numActive = myObjectPool.getNumActive();
        int numIdle = myObjectPool.getNumIdle();
        System.out.println("ThreadName:" + Thread.currentThread().getName() + " 活跃对象数量:" + numActive + " 空闲对象数:" + numIdle);
        System.out.println("------------------------------------------------------------");
    }
    
    private static void sleep() throws Exception{
        TimeUnit.SECONDS.sleep(1);
    }

}