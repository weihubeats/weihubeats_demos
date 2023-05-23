package com.skywalking.product.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author : wh
 * @date : 2023/5/23 14:35
 * @description:
 */
@Configuration
@Slf4j
public class MQConfig {

    @Bean(name = "consumer", initMethod = "start", destroyMethod = "shutdown")
    @Profile("!dev")
    public DefaultMQPushConsumer consumer() throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("gid-xiao-zou-topic");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
    
        consumer.subscribe("xiao-zou-topic", "*");

        consumer.registerMessageListener((MessageListenerConcurrently) (msg, context) -> {
            log.info("Receive New Messages {}", msg.toString());
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        return consumer;
    }




}
