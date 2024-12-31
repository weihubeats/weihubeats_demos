## kafka version
- kafka_2.13-3.5.0

## 背景

线上的`kafka`集群要进行扩容，原先的2`broker`，扩容之后变成了新增3个`broker`，然后下掉了原先老的`broker`。

新集群看着没问题，但是出现了一个问题。

消息发送都正常，但是消息消费异常


## 排查

### 消费端打印log

消费者启动后一直打印如下关键log
```java
2024-06-14 09:54:23 DEBUG o.a.k.c.c.i.ConsumerCoordinator:887 - [Consumer clientId=consumer-gid_abaca-1, groupId=gid_abaca] Received FindCoordinator response ClientResponse(receivedTimeMs=1718330063896, latencyMs=285, disconnected=false, timedOut=false, requestHeader=RequestHeader(apiKey=FIND_COORDINATOR, apiVersion=4, clientId=consumer-gid_xiaozou-1, correlationId=61, headerVersion=2), responseBody=FindCoordinatorResponseData(throttleTimeMs=0, errorCode=0, errorMessage='', nodeId=0, host='', port=0, coordinators=[Coordinator(key='gid_xiaozou', nodeId=-1, host='', port=-1, errorCode=15, errorMessage='')]))
2024-06-14 09:54:23 DEBUG o.a.k.c.c.i.ConsumerCoordinator:914 - [Consumer clientId=consumer-gid_abaca-1, groupId=gid_abaca] Group coordinator lookup failed: 
2024-06-14 09:54:23 DEBUG o.a.k.c.c.i.ConsumerCoordinator:276 - [Consumer clientId=consumer-gid_abaca-1, groupId=gid_abaca] Coordinator discovery failed, refreshing metadata
org.apache.kafka.common.errors.CoordinatorNotAvailableException: The coordinator is not available.
```

可以看到关键的log是`CoordinatorNotAvailableException: The coordinator is not available.`

### 查看消费者状态

进入到`kafka`进去`bin`目录执行如下脚本
```shell
./kafka-consumer-groups.sh --bootstrap-server <bootstrap.servers> --describe --group <your-consumer-group-id>
```

输出如下信息

```java
Error: Executing consumer group command failed due to org.apache.kafka.common.errors.TimeoutException: Call(callName=describeGroups(api=FIND_COORDINATOR), deadlineMs=1718330591913, tries=42094, nextAllowedTryMs=1718330592017) timed out at 1718330591917 after 42094 attempt(s)
java.util.concurrent.ExecutionException: org.apache.kafka.common.errors.TimeoutException: Call(callName=describeGroups(api=FIND_COORDINATOR), deadlineMs=1718330591913, tries=42094, nextAllowedTryMs=1718330592017) timed out at 1718330591917 after 42094 attempt(s)
        at java.base/java.util.concurrent.CompletableFuture.reportGet(CompletableFuture.java:395)
        at java.base/java.util.concurrent.CompletableFuture.get(CompletableFuture.java:2005)
        at org.apache.kafka.common.internals.KafkaFutureImpl.get(KafkaFutureImpl.java:165)
        at kafka.admin.ConsumerGroupCommand$ConsumerGroupService.$anonfun$describeConsumerGroups$1(ConsumerGroupCommand.scala:543)
        at scala.collection.StrictOptimizedMapOps.map(StrictOptimizedMapOps.scala:28)
        at scala.collection.StrictOptimizedMapOps.map$(StrictOptimizedMapOps.scala:27)
        at scala.collection.convert.JavaCollectionWrappers$AbstractJMapWrapper.map(JavaCollectionWrappers.scala:313)
        at kafka.admin.ConsumerGroupCommand$ConsumerGroupService.describeConsumerGroups(ConsumerGroupCommand.scala:542)
        at kafka.admin.ConsumerGroupCommand$ConsumerGroupService.collectGroupsOffsets(ConsumerGroupCommand.scala:558)
        at kafka.admin.ConsumerGroupCommand$ConsumerGroupService.describeGroups(ConsumerGroupCommand.scala:367)
        at kafka.admin.ConsumerGroupCommand$.run(ConsumerGroupCommand.scala:72)
        at kafka.admin.ConsumerGroupCommand$.main(ConsumerGroupCommand.scala:59)
        at kafka.admin.ConsumerGroupCommand.main(ConsumerGroupCommand.scala)
Caused by: org.apache.kafka.common.errors.TimeoutException: Call(callName=describeGroups(api=FIND_COORDINATOR), deadlineMs=1718330591913, tries=42094, nextAllowedTryMs=1718330592017) timed out at 1718330591917 after 42094 attempt(s)
Caused by: org.apache.kafka.common.errors.DisconnectException: Cancelled describeGroups(api=FIND_COORDINATOR) request with correlation id 42096 due to node 6 being disconnected
```

看着是查询不到`comsumer group`的信息，然后报了`TimeoutException`异常,没有什么有用的信息

### 什么情况会出现 The coordinator is not available

网上有一种说法是如果`broker`配置文件中的`offsets.topic.replication.factor`必须小于等于`broker`的数量，否则会出现`The coordinator is not available`的问题。

比如`broker`的数量是3，`offsets.topic.replication.factor`是4，就会出现这个问题。

实际我这边是不会出现这个问题，因为我的`broker`数量是3，`offsets.topic.replication.factor`配置的是2

### 删除topic重建是否可行

删除`topic`也没用，还是会出现`The coordinator is not available`的问题

### 是否是__consumer_offsets出了问题

`kafka`的消费位点主要通过`__consumer_offsets`这个topic来管理

由于本次为了迁移简单，同时线上的业务数据可以丢弃，所以并没有进行`topic`分区迁移，而是直接删除了`topic`，然后重建`topic`。

然后我们查看`__consumer_offsets`这个`topic`的分区情况
![consumer_offsets-topic.png](../images/consumer_offsets-topic.png)

点进`topic`详情发现 partition 还是仅在老的`broker`中，即已经下掉的topic。这就导致新的`broker`没有`__consumer_offsets`这个`topic`


## 解决方式

删除掉`__consumer_offsets`这个`topic`，然后系统会自动重建，就可以解决了。

如果想更平滑的方式也可以考虑对`__consumer_offsets`进行分区迁移，注意重建后使用原来的`comsumer group`好像消费还是会有问题，如果使用原来的`comsumer group`还是消费异常，换个`comsumer group`就好了

## 总结

如果kafka能正常发送消息，但是消费异常，一般是消费位点出现了问题，即管理消费位点`__consumer_offsets`的这个`toipc`

目前来看新增了3个`broker` kafka并没有自动对`__consumer_offsets`进行分区迁移，需要手动进行迁移

所以后续出现消费相关的问题可以优先检查`__consumer_offsets`这个`topic`的情况,毕竟`kafka`得消费位点都依赖于`__consumer_offsets`这个topic