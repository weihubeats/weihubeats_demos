package com.spring.boot.base.controller;

import com.spring.boot.base.aop.case3.XiaoZouAnnotation;
import lombok.RequiredArgsConstructor;

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
@RequiredArgsConstructor
public class TestController {

	@GetMapping("/test")
	@XiaoZouAnnotation
	public void test() {
		System.out.println("haha");
	}
}
