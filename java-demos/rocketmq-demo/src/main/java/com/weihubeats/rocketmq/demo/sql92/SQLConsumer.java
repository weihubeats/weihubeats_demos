package com.weihubeats.rocketmq.demo.sql92;

import com.weihubeats.rocketmq.demo.MQUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;

/**
 *@author : wh
 *@date : 2023/7/20 10:05
 *@description:
 */
public class SQLConsumer {

	public static String GID = "xiao-zou-gid";


	public static void main(String[] args) throws Exception {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(GID);
		String sql = "gray is not null and gray = 'dev1'";
		consumer.subscribe(MQUtils.TOPIC, MessageSelector.bySql(sql));
		consumer.registerMessageListener((MessageListenerConcurrently) (msg, context) -> {
			System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msg);
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		});
		/*
		 *  Launch the consumer instance.
		 */
		consumer.start();
		System.out.printf("Consumer Started.%n");

	}
}
