package com.spring.boot.apollo.controller;

import com.alibaba.fastjson.JSON;
import com.spring.boot.apollo.config.ConfigurationExample;
import com.spring.boot.apollo.config.TestProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *@author : wh
 *@date : 2022/11/14 16:39
 *@description:
 */
@RestController
@RequestMapping
@EnableConfigurationProperties(TestProperties.class)
@RequiredArgsConstructor
public class TestController {

	private final TestProperties testProperties;

	private final ConfigurationExample configurationExample;
	
	@GetMapping("/test")
	public void test() {
		System.out.println(testProperties);
		
	}
	
	@GetMapping("/test1")
	public String test1() {
		System.out.println(JSON.toJSONString(configurationExample));
		return JSON.toJSONString(configurationExample.getListListMap());
	}
}
