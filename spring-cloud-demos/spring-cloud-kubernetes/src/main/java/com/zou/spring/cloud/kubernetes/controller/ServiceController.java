package com.zou.spring.cloud.kubernetes.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *@author : wh
 *@date : 2022/11/2 15:10
 *@description:
 */
@RestController
@RequestMapping("/service/v1")
@RequiredArgsConstructor
@Slf4j
public class ServiceController {

	private final DiscoveryClient discoveryClient;

	@GetMapping("/service")
	public List<String> getServiceList(){
		return discoveryClient.getServices();
	}

	

}
