package com.zou.spring.cloud.namedContextFactory.config;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *@author : wh
 *@date : 2022/11/24 21:20
 *@description:
 */
public class NamedHttpClient {

	private final String serviceName;
	private final CloseableHttpClient httpClient;
	private final RequestConfig requestConfig;

	public NamedHttpClient(String serviceName, ClientConfig clientConfig) {
		this.serviceName = serviceName;
		this.requestConfig = clientConfig.getRequestConfig();
		this.httpClient = HttpClients.custom()
				.setDefaultRequestConfig(requestConfig)
				.build();
	}

	public String getServiceName() {
		return serviceName;
	}

	public CloseableHttpClient getHttpClient() {
		return httpClient;
	}

	public RequestConfig getRequestConfig() {
		return requestConfig;
	}
}
