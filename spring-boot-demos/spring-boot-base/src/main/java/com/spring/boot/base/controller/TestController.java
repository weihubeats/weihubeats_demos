package com.spring.boot.base.controller;

import com.spring.boot.base.aop.case3.XiaoZouAnnotation;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
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

	private final AtomicInteger atomicInteger = new AtomicInteger(0);

	@GetMapping("/test")
	@XiaoZouAnnotation
	public void test() throws Exception{
		System.out.println("线程 " + Thread.currentThread().getName() + " 时间 " + System.currentTimeMillis());
		TimeUnit.SECONDS.sleep(5);
		System.out.println("执行完成, 执行次数 " + atomicInteger.incrementAndGet());
		
	}
}
