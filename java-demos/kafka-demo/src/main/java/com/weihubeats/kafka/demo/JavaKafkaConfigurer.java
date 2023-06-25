package com.weihubeats.kafka.demo;

import java.util.Properties;

/**
 * @author : wh
 * @date : 2023/6/25 14:45
 * @description:
 */
public class JavaKafkaConfigurer {

    private static Properties properties;

    public synchronized static Properties getKafkaProperties() {
        if (null != properties) {
            return properties;
        }
        //获取配置文件kafka.properties的内容
        Properties kafkaProperties = new Properties();
        try {
            kafkaProperties.load(Producer.class.getClassLoader().getResourceAsStream("kafka.properties"));
        } catch (Exception e) {
            //没加载到文件，程序要考虑退出
            e.printStackTrace();
        }
        properties = kafkaProperties;
        return kafkaProperties;
    }

}
