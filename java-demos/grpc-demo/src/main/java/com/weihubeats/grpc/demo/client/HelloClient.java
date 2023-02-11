package com.weihubeats.grpc.demo.client;

import com.weihubeats.grpc.demo.HelloGrpc;
import com.weihubeats.grpc.demo.HelloRequest;
import com.weihubeats.grpc.demo.HelloResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import java.util.concurrent.TimeUnit;

/**
 * @author : wh
 * @date : 2023/2/11 10:04
 * @description:
 */
public class HelloClient {

    //远程连接管理器,管理连接的生命周期
    private final ManagedChannel channel;
    private final HelloGrpc.HelloBlockingStub blockingStub;



    public HelloClient(String host, int port) {
        //初始化连接
        channel = ManagedChannelBuilder.forAddress(host, port)
            .usePlaintext()
            .build();
        //初始化远程服务Stub
        blockingStub = HelloGrpc.newBlockingStub(channel);
    }


    public void shutdown() throws InterruptedException {
        //关闭连接
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public String sayHello(String name) {
        //构造服务调用参数对象
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        //调用远程服务方法
        HelloResponse response = blockingStub.sayHello(request);
        //返回值
        return response.getMessage();
    }


    public static void main(String[] args) throws InterruptedException {
        
        HelloClient client = new HelloClient("127.0.0.1", 50051);

        //服务调用
        String content = client.sayHello("Java client");
        //打印调用结果
        System.out.println(content);
        //关闭连接
        client.shutdown();
    }

}
