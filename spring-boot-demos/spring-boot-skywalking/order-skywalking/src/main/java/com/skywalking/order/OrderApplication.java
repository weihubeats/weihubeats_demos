package com.skywalking.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 *@author : wh
 *@date : 2022/11/11 18:09
 *@description:
 */
@SpringBootApplication
@EnableFeignClients
public class OrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderApplication.class, args);
	}
}
