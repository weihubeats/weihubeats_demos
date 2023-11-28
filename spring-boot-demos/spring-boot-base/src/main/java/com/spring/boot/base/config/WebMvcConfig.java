package com.spring.boot.base.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *@author : wh
 *@date : 2023/11/7 11:47
 *@description:
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		configurer.addPathPrefix("/order", c -> c.isAnnotationPresent(RestController.class) || c.isAnnotationPresent(Controller.class));
	}

}
