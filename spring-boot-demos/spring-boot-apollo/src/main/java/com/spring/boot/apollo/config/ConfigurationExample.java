package com.spring.boot.apollo.config;

import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author : wh
 * @date : 2023/11/20 10:04
 * @description: 各种配置Java Bean https://blog.csdn.net/qq_42651904/article/details/126947911
 */
@Configuration(proxyBeanMethods = false)
@Component
@Getter
public class ConfigurationExample {

    /**
     * 配置文件 test1,xiaozou,test2
     *//*
    @Value("${test.list}")
    private List<String> list;

    *//**
     * 配置文件 
     * {
     *     "test": "vlaue1,vlaue2",
     *     "xiaozou": "xiaozouValue,xiaozouValue1"
     * }
     */
    @Value("#{${test.map}}")
    private Map<String, List<String>> testMap;

    /**
     * {"1,2": "vlaue1,vlaue2", "3,4": "xiaozouValue,xiaozouValue1"}
     */
    @Value("#{${test.list.map}}")
    private Map<Set<Integer>, Set<String>> setSetMap;

    /**
     * {"1,2": "vlaue1,vlaue2", "3,4": "xiaozouValue,xiaozouValue1"}
     */
    @Value("#{${test.list.map}}")
    private Map<List<Integer>, List<String>> listListMap;




}
