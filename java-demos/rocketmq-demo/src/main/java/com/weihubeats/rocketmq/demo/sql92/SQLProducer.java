package com.weihubeats.rocketmq.demo.sql92;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.IntStream;

import com.weihubeats.rocketmq.demo.MQUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 *@author : wh
 *@date : 2023/7/20 09:56
 *@description:
 */
public class SQLProducer {

	public static int count = 10;

	public static String topic = "xiao-zou-topic";


	public static void main(String[] args) {
		DefaultMQProducer producer = MQUtils.createLocalProducer();
		
		IntStream.range(0, count).forEach(i -> {
			Message message = new Message(topic, ("sql92 test" + i).getBytes(StandardCharsets.UTF_8));
			try {
				if (i % 2 == 0) {
					message.putUserProperty("gray", "dev1");
				}
				SendResult sendResult = producer.send(message);
				DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				System.out.printf("%s %s%n", sendResult, dtf2.format(LocalDateTime.now()));
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		producer.shutdown();
		
	}
}
