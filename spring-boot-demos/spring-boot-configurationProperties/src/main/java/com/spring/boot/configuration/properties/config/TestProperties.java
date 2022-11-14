package com.spring.boot.configuration.properties.config;

import com.spring.boot.configuration.properties.properties.Student;
import lombok.Data;
import lombok.ToString;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 *@author : wh
 *@date : 2022/11/14 16:37
 *@description:
 */
@ConfigurationProperties("spring.xiaozou.test")
@ToString
@Data
public class TestProperties {
	
	private String name;

	@NestedConfigurationProperty
	private Student student;
}
