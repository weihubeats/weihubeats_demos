package com.zou.spring.cloud.namedContextFactory.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 *@author : wh
 *@date : 2022/11/24 21:14
 *@description:
 */
public class NamedHttpClientConfiguration {

	@Value("${http.client.name}")
	private String httpClientName; // 1.

	@Bean
	@ConditionalOnMissingBean
	public ClientConfig clientConfig(Environment env) {
		System.out.println("-----------------------------init ClientConfig");
		return new ClientConfig(httpClientName, env); // 2.
	}

	@Bean
	@ConditionalOnMissingBean
	public NamedHttpClient namedHttpClient(ClientConfig clientConfig) {
		System.out.println("-------------------------init NamedHttpClient");
		return new NamedHttpClient(httpClientName, clientConfig); // 3.
	}

}
