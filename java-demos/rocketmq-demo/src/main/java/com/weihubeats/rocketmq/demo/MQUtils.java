package com.weihubeats.rocketmq.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;

/**
 *@author : wh
 *@date : 2023/7/21 14:00
 *@description:
 */
public class MQUtils {

	public static final String PRODUCER_GROUP = "xiao-zou-topic-producer";

	public static final String DEFAULT_NAMESRVADDR = "127.0.0.1:9001";

	public static final String TOPIC = "xiao-zou-topic";


	public static DefaultMQProducer createLocalProducer() {
		return createProducer(PRODUCER_GROUP, DEFAULT_NAMESRVADDR);
	}


	public static DefaultMQProducer createProducer(String producerGroup, String namesrvAddr) {
		DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
		producer.setNamesrvAddr(namesrvAddr);
		try {
			producer.start();
		} catch (MQClientException e) {
			throw new RuntimeException(e);
		}
		return producer;
	}

	public static DefaultMQPushConsumer createLocalConsumer(String gid) {
		return createConsumer(DEFAULT_NAMESRVADDR, gid);
	}

	public static DefaultMQPushConsumer createConsumer(String namesrvAddr, String gid) {
		DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer();
		defaultMQPushConsumer.setNamesrvAddr(namesrvAddr);
		defaultMQPushConsumer.setConsumerGroup(gid);
		return defaultMQPushConsumer;
	}
	
}
