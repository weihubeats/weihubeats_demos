package com.weihubeats.commons.pool.demo;

/**
 * @author : wh
 * @date : 2023/6/5 14:11
 * @description:
 */
public class XiaoZou {

    private final String name;

    public XiaoZou(String name) {
        this.name = name;
    }

    public void create() {
        System.out.println("ThreadName:" + Thread.currentThread().getName() + " 对象:" + name + "正在被创建。。。。。。");

    }

    public void destroy() {
        System.out.println("ThreadName:" + Thread.currentThread().getName() + " 对象:" + name + "正在被销毁。。。。。。");

    }

    public boolean isValid() {
        System.out.println("ThreadName:" + Thread.currentThread().getName() + " 对象" + name + "正在检验是否可用。。。。。。");

        return true;
    }

}
