package com.weihubeats.grpc.demo.server;

import com.weihubeats.grpc.demo.HelloGrpc;
import com.weihubeats.grpc.demo.HelloRequest;
import com.weihubeats.grpc.demo.HelloResponse;
import io.grpc.stub.StreamObserver;

/**
 * @author : wh
 * @date : 2023/2/11 10:26
 * @description:
 */
public class HelloImpl extends HelloGrpc.HelloImplBase {
    @Override
    public void sayHello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        // super.sayHello(request, responseObserver);
        HelloResponse helloResponse=HelloResponse.newBuilder().setMessage("Hello "+request.getName()+", I'm Java grpc Server").build();
        responseObserver.onNext(helloResponse);
        responseObserver.onCompleted();
    }
}
