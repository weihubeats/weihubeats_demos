package com.skywalking.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author : wh
 * @date : 2023/5/23 11:28
 * @description:
 */
@FeignClient(name = "order", url = "http://localhost:8090")
public interface OrderClient {


    @GetMapping("/product/get")
     String get(@RequestParam("name") String name);
    
    
}
