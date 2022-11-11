package com.jib.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *@author : wh
 *@date : 2022/11/11 18:11
 *@description:
 */
@RestController
public class TestController {

	@RequestMapping("/hello")
	public String hello(){
		return "Hello, " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}
}
