package com.xiaozou.security.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author : wh
 * @date : 2025/2/19 17:52
 * @description:
 */
@Controller
public class LoginController {
    
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // 对应模板文件名（不带后缀）
    }
}