package com.spring.boot.base.aop.case3;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 *@author : wh
 *@date : 2023/11/6 16:16
 *@description:
 */
public class XiaoZouAnnotationInterceptor implements MethodInterceptor {
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
//		System.out.println("进来了");
		Method method = invocation.getMethod();
//		System.out.println("进来了");
		return invocation.proceed();
	}
}
