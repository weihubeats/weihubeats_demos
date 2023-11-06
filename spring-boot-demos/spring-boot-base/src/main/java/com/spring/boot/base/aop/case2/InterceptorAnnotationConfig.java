/*
package com.spring.boot.base.aop.case2;

import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

*/
/**
 *@author : wh
 *@date : 2023/11/6 11:27
 *@description:
 *//*

@Configuration
public class InterceptorAnnotationConfig {

	@Bean
	@Order(Integer.MIN_VALUE)
	public DefaultPointcutAdvisor annotationPointcutAdvisor() {
		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
		//针对EasyExceptionResult注解创建切点
		AnnotationMatchingPointcut annotationMatchingPointcut = new AnnotationMatchingPointcut(XiaoZouAnnotation.class, true);
		XiaoZouAnnotationInterceptor interceptor = new XiaoZouAnnotationInterceptor();
		advisor.setPointcut(annotationMatchingPointcut);
		//在切点执行interceptor中的invoke方法
		advisor.setAdvice(interceptor);
		return advisor;
	}


}
*/
