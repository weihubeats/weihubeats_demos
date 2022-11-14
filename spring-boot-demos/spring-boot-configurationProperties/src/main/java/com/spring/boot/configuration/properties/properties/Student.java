package com.spring.boot.configuration.properties.properties;

import java.util.List;

import lombok.Data;

import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 *@author : wh
 *@date : 2022/11/14 16:34
 *@description:
 */
@Data
public class Student {
	
	private String name;

	@NestedConfigurationProperty
	private List<Teacher> teachers;
}
