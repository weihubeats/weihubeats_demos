package com.spring.boot.base.conditionalOnBeanTest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wh
 * @date : 2024/1/4 14:34
 * @description:
 */
@Configuration
public class Configuration1 {

    @Bean
    @ConditionalOnBean(Bean2.class)
    public Bean1 bean1() {
        System.out.println("bean1");
        return new Bean1();
    }
}
