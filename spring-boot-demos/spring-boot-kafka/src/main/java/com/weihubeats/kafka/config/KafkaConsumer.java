package com.weihubeats.kafka.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author : wh
 * @date : 2023/12/22 15:08
 * @description:
 */
@Component
@Slf4j
public class KafkaConsumer {

    @KafkaListener(topics = "xiaozou", groupId = "gid_xiaozou")
    public void consumeMsg(ConsumerRecord<?, ?> record) {
        try {
            log.info("consumer kafka msg: " + record.value());
        } catch (Exception e) {
            log.info("consumer kafka eroor ", e);
        }
    }

}
