package com.skywalking.order.controller;

import com.skywalking.order.feign.OrderClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.skywalking.apm.toolkit.trace.RunnableWrapper;
import org.apache.skywalking.apm.toolkit.trace.SupplierWrapper;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * @author : wh
 * @date : 2023/5/22 17:44
 * @description:
 */
@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderClient orderClient;

    private final DefaultMQProducer producer;

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    @GetMapping("/get")
    public String get(String name) {
        log.info("order get name:{}", name);
        return "order:" + name;
    }

    @GetMapping("/rpc")
    public String rpc(String name) throws Exception{
        log.info("order get name:{}", name);

        Message message = new Message();
        message.setBody("小奏技术 rocketmq 消息测试".getBytes());
        message.setTopic("xiao-zou-topic");
        message.setTags("xiao-zou-tag");
        producer.send(message);
        orderClient.get(name);
        return "order:" + name;
    }

    @GetMapping("/executorService")
    public void executorService() {
        log.info("test log parent traceId:{}", TraceContext.traceId());

        // 异步线程
        Thread thread = new Thread(() -> {
            log.info("test thread log thread traceId:{}", TraceContext.traceId());
        });
        thread.start();

        Thread thread1 = new Thread(new RunnableWrapper(() -> log.info("test thread1 log thread traceId:{}", TraceContext.traceId())));
        thread1.start();

        CompletableFuture.runAsync(() -> {
            log.info("test runAsync log CompletableFuture traceId:{}", TraceContext.traceId());
        });

        CompletableFuture.supplyAsync(()->{
            log.info("test supplyAsync log CompletableFuture traceId:{}", TraceContext.traceId());
            return "SupplierWrapper";
        }).thenAccept(System.out::println);

        CompletableFuture.supplyAsync(SupplierWrapper.of(()->{
            log.info("supplyAsync SupplierWrapper log CompletableFuture traceId:{}", TraceContext.traceId());
            return "SupplierWrapper";
        })).thenAccept(System.out::println);

        IntStream.rangeClosed(1, 3).parallel().forEach(i -> {
            log.info("test log IntStream traceId:{}", TraceContext.traceId());
        });

        // 线程池线程
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(() -> {
            log.info("test log executorService traceId:{}", TraceContext.traceId());
        });
        executorService.shutdown();
    }
    
    
    
    
}
