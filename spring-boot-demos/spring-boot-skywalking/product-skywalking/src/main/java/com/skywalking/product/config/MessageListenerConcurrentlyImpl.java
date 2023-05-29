package com.skywalking.product.config;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author : wh
 * @date : 2023/5/26 10:31
 * @description:
 */
@Slf4j
public class MessageListenerConcurrentlyImpl implements MessageListenerConcurrently {
    
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msg, ConsumeConcurrentlyContext context) {
        log.info("Receive New Messages {}", msg.toString());
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
