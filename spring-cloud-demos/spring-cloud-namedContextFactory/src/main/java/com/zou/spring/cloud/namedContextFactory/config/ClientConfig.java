package com.zou.spring.cloud.namedContextFactory.config;

import org.apache.http.client.config.RequestConfig;

import org.springframework.core.env.Environment;

/**
 *@author : wh
 *@date : 2022/11/24 21:17
 *@description:
 */
public class ClientConfig {

	private final String serviceName;

	private final Environment env;

	public ClientConfig(String serviceName, Environment env) {
		this.serviceName = serviceName;
		this.env = env;
	}

	public RequestConfig getRequestConfig() {
		Integer socketTimeout = env.getProperty(serviceName + "." + "socketTimeout", Integer.class);
		if (socketTimeout != null) {
			return RequestConfig.custom().setSocketTimeout(socketTimeout).build();
		}

		return RequestConfig.custom().build();
	}
}
