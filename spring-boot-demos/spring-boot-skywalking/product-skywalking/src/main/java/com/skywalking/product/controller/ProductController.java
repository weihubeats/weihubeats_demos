package com.skywalking.product.controller;

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
@RequestMapping("/product")
@Slf4j
public class ProductController {

    @GetMapping("/get")
    public String get(String name) {
        log.info("product get name:{}", name);
        return "product:" + name;
    }
}
