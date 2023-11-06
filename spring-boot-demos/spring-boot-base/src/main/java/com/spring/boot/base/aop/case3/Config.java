package com.spring.boot.base.aop.case3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *@author : wh
 *@date : 2023/11/6 16:17
 *@description:
 */
@Configuration
public class Config {

	@Bean
	public XiaZouAnnotationAdvisor XiaZouAnnotationAdvisor() {
		XiaoZouAnnotationInterceptor interceptor = new XiaoZouAnnotationInterceptor();
		XiaZouAnnotationAdvisor xiaZouAnnotationAdvisor = new XiaZouAnnotationAdvisor(interceptor, XiaoZouAnnotation.class);
		xiaZouAnnotationAdvisor.setOrder(Integer.MIN_VALUE);
		return xiaZouAnnotationAdvisor;
	}
}
