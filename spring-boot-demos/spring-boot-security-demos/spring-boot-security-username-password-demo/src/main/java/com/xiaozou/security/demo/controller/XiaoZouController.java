package com.xiaozou.security.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : wh
 * @date : 2025/2/19 15:07
 * @description:
 */
@RestController
@RequestMapping()
public class XiaoZouController {


    @GetMapping("/public")
    public String publicHello() {
        return "Public Hello!";
    }

    @GetMapping("/admin/hello")
    public String adminHello() {
        return "Admin Hello!";
    }

    @GetMapping("/home")
    public String home() {
        return "Welcome!";
    }

}
