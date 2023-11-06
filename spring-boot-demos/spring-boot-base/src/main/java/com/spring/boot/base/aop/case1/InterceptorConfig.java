/*
package com.spring.boot.base.aop.case1;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

*/
/**
 *@author : wh
 *@date : 2023/11/6 11:21
 *@description:
 *//*

@Configuration
public class InterceptorConfig {

	public static final String traceExecution = "execution(* com.spring.boot.base..*.*(..))";


	@Bean
	public DefaultPointcutAdvisor defaultPointcutAdvisor() {
		XiaozouMethodInterceptor interceptor = new XiaozouMethodInterceptor();
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(traceExecution);
		// 配置增强类advisor
		DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
		advisor.setPointcut(pointcut);
		advisor.setAdvice(interceptor);
		return advisor;
	}

	
}
*/
