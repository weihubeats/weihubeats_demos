package com.spring.boot.base.conditionalOnBeanTest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : wh
 * @date : 2024/1/4 14:34
 * @description:
 */
@Configuration
public class Configuration2 {

    @Bean
    public Bean2 bean2(){
        System.out.println("bean2");
        return new Bean2();
    }
    
}
