package com.spring.boot.base.config;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author : wh
 * @date : 2024/1/12 11:21
 * @description:
 */
@Configuration
public class TestConfig implements SmartInitializingSingleton, EnvironmentAware {

//    @Value("${spring.application.name}")
//    private String applicationName;

    private Environment environment;

    @Override
    public void afterSingletonsInstantiated() {

        String applicationName = environment.getProperty("spring.application.name");
        System.out.println("applicationName:" + applicationName);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
