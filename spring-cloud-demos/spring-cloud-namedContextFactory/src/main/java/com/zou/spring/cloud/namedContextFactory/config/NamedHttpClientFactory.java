package com.zou.spring.cloud.namedContextFactory.config;

import org.springframework.cloud.context.named.NamedContextFactory;
import org.springframework.stereotype.Component;

/**
 *@author : wh
 *@date : 2022/11/24 21:21
 *@description:
 */
@Component
public class NamedHttpClientFactory extends NamedContextFactory<NamedHttpClientSpec> {

	public NamedHttpClientFactory() {
		super(NamedHttpClientConfiguration.class, "namedHttpClient", "http.client.name");
	}

}
