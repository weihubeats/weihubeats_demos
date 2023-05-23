package com.skywalking.order.controller;

import com.skywalking.order.feign.OrderClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wh
 * @date : 2023/5/22 17:44
 * @description:
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderClient orderClient;

    @GetMapping("/get")
    public String get(String name) {
        log.info("order get name:{}", name);
        return "order:" + name;
    }

    @GetMapping("/rpc")
    public String rpc(String name) {
        log.info("order get name:{}", name);
        orderClient.get(name);
        return "order:" + name;
    }
    
    
    
    
}
