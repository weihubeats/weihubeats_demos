package com.spring.boot.apollo.config;

import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Configuration;

/**
 *@author : wh
 *@date : 2023/1/14 10:55
 *@description:
 */
@Configuration
@Slf4j
public class ApolloPropertiesChangedListener implements ApplicationEventPublisherAware {

	public static final String NAMESPACE = "test.yml";


	private ApplicationEventPublisher publisher;

	@ApolloConfigChangeListener(value = NAMESPACE)
	public void onChange(ConfigChangeEvent changeEvent) {
		publisher.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
	}

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.publisher = applicationEventPublisher;
	}
}
