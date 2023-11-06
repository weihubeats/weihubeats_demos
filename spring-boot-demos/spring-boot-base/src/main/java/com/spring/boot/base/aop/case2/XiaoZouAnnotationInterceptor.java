/*
package com.spring.boot.base.aop.case2;

import java.lang.reflect.Method;

import com.spring.boot.base.aop.case3.XiaoZouAnnotation;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

*/
/**
 *@author : wh
 *@date : 2023/11/6 11:24
 *@description:
 *//*

public class XiaoZouAnnotationInterceptor implements MethodInterceptor {
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();

		XiaoZouAnnotation xiaoZouAnnotation = method.getAnnotation(XiaoZouAnnotation.class);
		if (xiaoZouAnnotation == null) {
			return invocation.proceed();
		}
		System.out.println("method " + invocation.getMethod() + " is called on " + invocation.getThis() + " with args" +
				" " + invocation.getArguments());
		Object proceed = invocation.proceed();
		System.out.println("method " + invocation.getMethod() + " returns " + proceed);
		return proceed;

	}
}
*/
