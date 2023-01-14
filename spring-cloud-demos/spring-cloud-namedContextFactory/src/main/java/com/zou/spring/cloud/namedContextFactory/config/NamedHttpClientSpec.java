package com.zou.spring.cloud.namedContextFactory.config;

import org.springframework.cloud.context.named.NamedContextFactory;

/**
 *@author : wh
 *@date : 2022/11/24 21:14
 *@description:
 */
public class NamedHttpClientSpec implements NamedContextFactory.Specification {

	private final String name;
	private final Class<?>[] configuration;

	public NamedHttpClientSpec(String name, Class<?>[] configuration) {
		this.name = name;
		this.configuration = configuration;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<?>[] getConfiguration() {
		return configuration;
	}
}
