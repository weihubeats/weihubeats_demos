package com.spring.boot.configuration.properties.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author : wh
 * @date : 2023/2/14 17:45
 * @description:
 */
@ConfigurationProperties("meta.test")
@ToString
@Data
public class Test1Properties {
    
    private String name;
}
