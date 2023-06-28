package com.weihubeats.kafka.demo;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.Future;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 *@author : wh
 *@date : 2023/6/26 19:54
 *@description:
 */
public class KafkaExample {

	private static final String TOPIC = "quickstart-events";
	private static final String BOOTSTRAP_SERVERS = "127.0.0.1:9092";
	
	private static final String GROUP_ID = "testGid";

	public static void main(String[] args) {
		new Thread(KafkaExample::consumeMessage).start();
		// 生产消息
		produceMessage();

		// 消费消息
//		consumeMessage();
	}

	private static void produceMessage() {
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		Producer<String, String> producer = new KafkaProducer<>(props);

		try {
			for (int i = 0; i < 10; i++) {
				String message = "Message " + i;
				System.out.println("开始发送消息");
				Future<RecordMetadata> send = producer.send(new ProducerRecord<>(TOPIC, message));
				RecordMetadata recordMetadata = send.get();
				System.out.println("Produced message: " + message);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			producer.close();
		}
	}

	private static void consumeMessage() {
		System.out.println("消费消息开始");
		Properties props = new Properties();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
		props.put("group.id", GROUP_ID);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		Consumer<String, String> consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Collections.singletonList(TOPIC));

		try {
			while (true) {
				ConsumerRecords<String, String> poll = consumer.poll(Duration.ofSeconds(1));
				poll.forEach(record -> System.out.println("Consumed message: " + record.value()));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			consumer.close();
		}
	}

}
