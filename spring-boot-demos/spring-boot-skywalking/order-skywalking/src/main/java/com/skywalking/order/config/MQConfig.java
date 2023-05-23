package com.skywalking.order.config;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author : wh
 * @date : 2023/5/23 14:35
 * @description:
 */
@Configuration
public class MQConfig {

    @Bean(name = "producer", initMethod = "start", destroyMethod = "shutdown")
    @Profile("!dev")
    public DefaultMQProducer producer() {
        DefaultMQProducer producer = new DefaultMQProducer("xiao-zou-topic-producer");
        producer.setNamesrvAddr("127.0.0.1:9876");
        return producer;
    }




}
