package com.weihubeats.commons.pool.demo;

import java.util.UUID;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @author : wh
 * @date : 2023/6/5 14:10
 * @description:
 */
public class XiaoZouBasePooledObjectFactory extends BasePooledObjectFactory<XiaoZou> {

    @Override
    public XiaoZou create() {
        // 创建一个新的MyObject对象
        XiaoZou myObject = new XiaoZou(UUID.randomUUID().toString());
        myObject.create();
        return myObject;
    }

    @Override
    public PooledObject<XiaoZou> wrap(XiaoZou myObject) {
        // 将MyObject对象封装到一个PooledObject对象中并返回
        return new DefaultPooledObject<>(myObject);
    }

    @Override
    public void destroyObject(PooledObject<XiaoZou> pooledObject) throws Exception {
        // 销毁对象
        XiaoZou myObject = pooledObject.getObject();
        myObject.destroy();
    }

    @Override
    public boolean validateObject(PooledObject<XiaoZou> pooledObject) {
        // 验证对象是否可用
        XiaoZou myObject = pooledObject.getObject();
        return myObject.isValid();
    }

}
