package com.zou.spring.boot.fabric8.maven.plugin.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *@author : wh
 *@date : 2022/11/16 16:35
 *@description:
 */
@RestController
public class TestController {

	@RequestMapping("/health")
	public String health() {
		return "OK";
	}

	@RequestMapping("/hello")
	public String hello(){
		return "Hello, " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
}
