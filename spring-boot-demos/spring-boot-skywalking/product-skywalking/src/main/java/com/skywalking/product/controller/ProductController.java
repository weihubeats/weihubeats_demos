package com.skywalking.product.controller;

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
public class ProductController {

    @GetMapping("/get")
    public String get(Integer id) {
        return "product:" + id;
    }
}
