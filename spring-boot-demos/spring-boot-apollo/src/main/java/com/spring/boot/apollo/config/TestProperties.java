package com.spring.boot.apollo.config;

import lombok.Data;
import lombok.ToString;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *@author : wh
 *@date : 2023/1/14 10:56
 *@description:
 */
@ConfigurationProperties("spring.cloud.test")
@ToString
@Data
public class TestProperties {
	
	private String name;
}
