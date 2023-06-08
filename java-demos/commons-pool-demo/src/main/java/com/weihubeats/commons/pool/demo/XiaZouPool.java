package com.weihubeats.commons.pool.demo;

import java.time.Duration;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author : wh
 * @date : 2023/6/5 14:11
 * @description:
 */
public enum XiaZouPool {

    /**
     * 线程安全的单例
     */
    INSTANCE;

    private final GenericObjectPool<XiaoZou> objectPool;

    XiaZouPool() {
        // 创建对象池配置
        GenericObjectPoolConfig<XiaoZou> poolConfig = new GenericObjectPoolConfig<>();
        // 对象池中最大对象数
        poolConfig.setMaxTotal(3);
        // 对象池中最小空闲对象数
        poolConfig.setMinIdle(1);
        // 对象池中最大空闲对象数
        poolConfig.setMaxIdle(2);
        // 当对象池耗尽时，是否等待获取对象
        poolConfig.setBlockWhenExhausted(true);
        // 创建对象时是否进行对象有效性检查
        poolConfig.setTestOnCreate(true);
        // 借出对象时是否进行对象有效性检查
        poolConfig.setTestOnBorrow(true);
        // 归还对象时是否进行对象有效性检查
        poolConfig.setTestOnReturn(true);
        // 空闲时是否进行对象有效性检查
        poolConfig.setTestWhileIdle(true);
        // 获取对象最大等待时间 默认 -1 一直等待
        poolConfig.setMaxWait(Duration.ofSeconds(2));
        // 创建对象工厂
        XiaoZouBasePooledObjectFactory objectFactory = new XiaoZouBasePooledObjectFactory();
        // 创建对象池
        objectPool = new GenericObjectPool<>(objectFactory, poolConfig);
    }

    /**
     * 从对象池中借出一个对象
     * @return
     * @throws Exception
     */
    public XiaoZou borrowObject() throws Exception {
        XiaoZou zou = objectPool.borrowObject();
        int numActive = objectPool.getNumActive();
        int numIdle = objectPool.getNumIdle();
        System.out.println("ThreadName:" + Thread.currentThread().getName() + " 活跃对象数量:" + numActive + " 空闲对象数量:" + numIdle);
        System.out.println("------------------------------------------------------------");
        return zou;
    }

    public void returnObject(XiaoZou myObject) {
        // 将对象归还给对象池
        objectPool.returnObject(myObject);
    }

    /**
     * 获取活跃的对象数
     * @return
     */
    public int getNumActive() {
        return objectPool.getNumActive();
    }

    /**
     * 获取空闲的对象数
     * @return
     */
    public int getNumIdle()   {
        return objectPool.getNumIdle();
    }

}
