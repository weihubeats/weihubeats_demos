package com.spring.boot.duubo.provider;

import com.spring.boot.duubo.interface1.DemoService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author : wh
 * @date : 2023/11/13 11:25
 * @description:
 */
@DubboService
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
