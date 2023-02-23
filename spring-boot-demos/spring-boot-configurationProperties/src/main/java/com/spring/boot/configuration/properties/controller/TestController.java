package com.spring.boot.configuration.properties.controller;

import com.spring.boot.configuration.properties.config.Test1Properties;
import com.spring.boot.configuration.properties.config.TestProperties;
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
@EnableConfigurationProperties({TestProperties.class, Test1Properties.class})
@RequiredArgsConstructor
public class TestController {

	private final TestProperties testProperties;

	private final Test1Properties test1Properties;
	
	@GetMapping("/test")
	public void test() {
		System.out.println(testProperties);
		
	}

	@GetMapping("/test1")
	public void test1() {
		System.out.println(test1Properties);
	}
}
