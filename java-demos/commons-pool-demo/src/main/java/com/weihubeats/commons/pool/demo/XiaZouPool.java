package com.weihubeats.commons.pool.demo;

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
        poolConfig.setMaxTotal(8);
        // 对象池中最小空闲对象数
        poolConfig.setMinIdle(2);
        // 对象池中最大空闲对象数
        poolConfig.setMaxIdle(4);
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
        // 创建对象工厂
        XiaoZouBasePooledObjectFactory objectFactory = new XiaoZouBasePooledObjectFactory();
        // 创建对象池
        objectPool = new GenericObjectPool<>(objectFactory, poolConfig);
    }

    public XiaoZou borrowObject() throws Exception {
        // 从对象池中借出一个对象
        return objectPool.borrowObject();
    }

    public void returnObject(XiaoZou myObject) {
        // 将对象归还给对象池
        objectPool.returnObject(myObject);
    }

    public int getNumActive() {
        // 获取活跃的对象数
        return objectPool.getNumActive();
    }

    public int getNumIdle()   {
        // 获取空闲的对象数
        return objectPool.getNumIdle();
    }

}
