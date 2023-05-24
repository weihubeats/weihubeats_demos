package com.weihubeats.bytebuddy.demo;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.Test;

/**
 * @author : wh
 * @date : 2023/5/24 17:23
 * @description:
 */
public class AgentTest {
    
    private final static String path = new File("").getAbsolutePath();

    private final static String packageName = "/Users/weihu/Desktop/sofe/java/weihubeats_demos/java-demos/src/main/java/com/weihubeats/agent/demo/";

    private final static File FILE = new File(packageName);
    private final static String name = "com.weihubeats.HelloWorld";
    

    @Test
    public void test() throws Exception {
        Object helloWorld = new ByteBuddy()
            .subclass(Object.class)
            .name("com.weihubeats.HelloWorld")
            .make()
            .load(ClassLoader.getSystemClassLoader())
            .getLoaded()
            .newInstance();
    }

    @Test
    public void test1() throws Exception {
        String path = new File("").getAbsolutePath();
        System.out.println("path " + path);
        new ByteBuddy()
            .subclass(Object.class)
            .name("com.weihubeats.HelloWorld")
            .make()
            .saveIn(new File(path));
    }

    @Test
    public void test2() throws Exception {
        Object helloWorld = new ByteBuddy()
            .subclass(Zou.class)
            .name(name)
            .make()
            .saveIn(FILE);
    }

    @Test
    public void test3() throws Exception {
        Object helloWorld = new ByteBuddy()
            .subclass(Object.class)
            .name(name)
            .defineMethod("helloWorld", String.class, Modifier.PUBLIC)//定义一个方法
            .intercept(FixedValue.value("修改 helloWorld 输出值为 拦截 hello world"))// 拦截方法
            .make()
            .load(ClassLoader.getSystemClassLoader())
            .getLoaded()
            .newInstance();

        Method method = helloWorld.getClass().getMethod("helloWorld");
        System.out.println(method.invoke(helloWorld));
    }
    
    @Test
    public void test4() throws Exception {
        Zou agent = new ByteBuddy()
            .subclass(Zou.class)
            .method(ElementMatchers.named("testAgent"))
            .intercept(MethodDelegation.to(ZouProxy.class))
            .make()
            .load(getClass().getClassLoader())
            .getLoaded()
            .newInstance();

        agent.testAgent();
    }
    
    @Test
    public void testAgent() {
        System.out.println("小奏技术");
        
    }






}