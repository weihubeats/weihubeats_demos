> 这里是**weihubeats**,觉得文章不错可以关注公众号**小奏技术**，文章首发。拒绝营销号，拒绝标题党


## RocketMQ版本
- 5.1.0


## 普通消息

消息重试的的实现分并普通消息和顺序消息。两者的重试机制大同小异。我们这里先看看不同消息


![](https://img-blog.csdnimg.cn/img_convert/40571782e6ac332d8c9a9d7d08fd29c7.png)


![](https://img-blog.csdnimg.cn/img_convert/04e41e8213c51ce45303aafc81be1183.png)

这里是官网定义的消息重试次数以及时间间隔。

有没有发现一个问题，这个重试消息的时间间隔和延时消息的时间间隔这么类似？


![](https://img-blog.csdnimg.cn/img_convert/80444c21eba93e114d56c572bd14036c.png)

> 图片来源[官网](https://rocketmq.apache.org/zh/docs/4.x/producer/04message3/)

待会我们进行源码分析就知道了。这里先卖个关子

## client源码分析

### ConsumeMessageConcurrentlyService

普通消息消费以及重试的逻辑在`ConsumeMessageConcurrentlyService`中，如果是顺序消息则是`ConsumeMessageOrderlyService`

具体的逻辑是`org.apache.rocketmq.client.impl.consumer.ConsumeMessageConcurrentlyService.ConsumeRequest#run`


由于今天我们的重点关注点是消息如何重试的。所以我们在消息消费的一些其他细节会略过

首先消息消费的状态由`ConsumeConcurrentlyStatus` 这个枚举控制

![](https://img-blog.csdnimg.cn/img_convert/e0203ac502539a29b290ff73791fc735.png)

注释很清晰
- CONSUME_SUCCESS 成功消费
- RECONSUME_LATER 消费时间，等待重试



![](https://img-blog.csdnimg.cn/img_convert/92e63c90d5a8c60c3083147d9a924f50.png)

可以看到消息消费主要是在
```java
status = listener.consumeMessage(Collections.unmodifiableList(msgs), context);
```
这里的`listener`就是实现了`MessageListener`接口的类。也就是我们在编写`consumer`需要传入的一个对象

比如像这种
```java
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(CONSUMER_GROUP, true);
        consumer.setNamesrvAddr(DEFAULT_NAMESRVADDR);
//        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        consumer.subscribe(TOPIC, "*");
        consumer.registerMessageListener((MessageListenerConcurrently) (msg, context) -> {
            System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msg);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        System.out.printf("Consumer Started.%n");
```



需要注意的是虽然消费状态只有成功和失败。但是消费结果却是由`ConsumeReturnType`这个枚举类定义的

- SUCCESS 成功
- TIME_OUT 超时
- EXCEPTION 异常
- RETURNNULL 消费结果返回null
- FAILED 消费结果返回失败

可以看到这里把消费成功还是失败再次作了细分
```java
          if (null == status) {
                if (hasException) {
                    returnType = ConsumeReturnType.EXCEPTION;
                } else {
                    returnType = ConsumeReturnType.RETURNNULL;
                }
            } else if (consumeRT >= defaultMQPushConsumer.getConsumeTimeout() * 60 * 1000) {
                returnType = ConsumeReturnType.TIME_OUT;
            } else if (ConsumeConcurrentlyStatus.RECONSUME_LATER == status) {
                returnType = ConsumeReturnType.FAILED;
            } else if (ConsumeConcurrentlyStatus.CONSUME_SUCCESS == status) {
                returnType = ConsumeReturnType.SUCCESS;
            }
```


我们继续回归主线，看看如果消费状态为`RECONSUME_LATER`会作什么处理

```java
            if (!processQueue.isDropped()) {
                ConsumeMessageConcurrentlyService.this.processConsumeResult(status, context, this);
            } else {
                log.warn("processQueue is dropped without process consume result. messageQueue={}, msgs={}", messageQueue, msgs);
            }
```

这里可以看到`processConsumeResult`方法对消息结果进行了处理。我们来看看这个方法

#### processConsumeResult

```java
        switch (this.defaultMQPushConsumer.getMessageModel()) {
            case BROADCASTING:
                for (int i = ackIndex + 1; i < consumeRequest.getMsgs().size(); i++) {
                    MessageExt msg = consumeRequest.getMsgs().get(i);
                    log.warn("BROADCASTING, the message consume failed, drop it, {}", msg.toString());
                }
                break;
            case CLUSTERING:
                List<MessageExt> msgBackFailed = new ArrayList<>(consumeRequest.getMsgs().size());
                for (int i = ackIndex + 1; i < consumeRequest.getMsgs().size(); i++) {
                    MessageExt msg = consumeRequest.getMsgs().get(i);
                    // Maybe message is expired and cleaned, just ignore it.
                    if (!consumeRequest.getProcessQueue().containsMessage(msg)) {
                        log.info("Message is not found in its process queue; skip send-back-procedure, topic={}, "
                                + "brokerName={}, queueId={}, queueOffset={}", msg.getTopic(), msg.getBrokerName(),
                            msg.getQueueId(), msg.getQueueOffset());
                        continue;
                    }
                    boolean result = this.sendMessageBack(msg, context);
                    if (!result) {
                        msg.setReconsumeTimes(msg.getReconsumeTimes() + 1);
                        msgBackFailed.add(msg);
                    }
                }

                if (!msgBackFailed.isEmpty()) {
                    consumeRequest.getMsgs().removeAll(msgBackFailed);

                    this.submitConsumeRequestLater(msgBackFailed, consumeRequest.getProcessQueue(), consumeRequest.getMessageQueue());
                }
                break;
            default:
                break;
        }

```

这里可以看到如果是`BROADCASTING`(广播消息)则只是打印了`warn`log，什么处理也不会干

我们来看看集群消息

注意这里的循环条件
```java
for (int i = ackIndex + 1; i < consumeRequest.getMsgs().size(); i++)
```

如果所有消息消费成功则 `ackIndex` = `consumeRequest.getMsgs()`.不会有消息进行下面的逻辑处理。如果有消息消费失败，才会进行下面的处理

看看下面的处理逻辑
```java
                    boolean result = this.sendMessageBack(msg, context);
                    if (!result) {
                        msg.setReconsumeTimes(msg.getReconsumeTimes() + 1);
                        msgBackFailed.add(msg);
                    }
```

1. 发送消费失败消息给`broker`
2. 如果消息发送给`broker`失败则将消息丢到`msgBackFailed`。然后再`client`自己进行消息重新消费，重试次数reconsumeTimes + 1

我们来看看`sendMessageBack`的处理逻辑

```java
public boolean sendMessageBack(final MessageExt msg, final ConsumeConcurrentlyContext context) {
        int delayLevel = context.getDelayLevelWhenNextConsume();

        // Wrap topic with namespace before sending back message.
        msg.setTopic(this.defaultMQPushConsumer.withNamespace(msg.getTopic()));
        try {
            this.defaultMQPushConsumerImpl.sendMessageBack(msg, delayLevel, this.defaultMQPushConsumer.queueWithNamespace(context.getMessageQueue()));
            return true;
        } catch (Exception e) {
            log.error("sendMessageBack exception, group: " + this.consumerGroup + " msg: " + msg, e);
        }

        return false;
    }
```

这里有实际`delayLevel`延时级别一直是0。

实际发送消息到`broker`是
```java
this.defaultMQPushConsumerImpl.sendMessageBack(msg, delayLevel, this.defaultMQPushConsumer.queueWithNamespace(context.getMessageQueue()));
```
我们看看`sendMessageBack`的具体逻辑

```java
private void sendMessageBack(MessageExt msg, int delayLevel, final String brokerName, final MessageQueue mq)
        throws RemotingException, MQBrokerException, InterruptedException, MQClientException {
        boolean needRetry = true;
        try {
            if (brokerName != null && brokerName.startsWith(MixAll.LOGICAL_QUEUE_MOCK_BROKER_PREFIX)
                || mq != null && mq.getBrokerName().startsWith(MixAll.LOGICAL_QUEUE_MOCK_BROKER_PREFIX)) {
                needRetry = false;
                sendMessageBackAsNormalMessage(msg);
            } else {
                String brokerAddr = (null != brokerName) ? this.mQClientFactory.findBrokerAddressInPublish(brokerName)
                    : RemotingHelper.parseSocketAddressAddr(msg.getStoreHost());
                this.mQClientFactory.getMQClientAPIImpl().consumerSendMessageBack(brokerAddr, brokerName, msg,
                    this.defaultMQPushConsumer.getConsumerGroup(), delayLevel, 5000, getMaxReconsumeTimes());
            }
        } catch (Throwable t) {
            log.error("Failed to send message back, consumerGroup={}, brokerName={}, mq={}, message={}",
                this.defaultMQPushConsumer.getConsumerGroup(), brokerName, mq, msg, t);
            if (needRetry) {
                sendMessageBackAsNormalMessage(msg);
            }
        } finally {
            msg.setTopic(NamespaceUtil.withoutNamespace(msg.getTopic(), this.defaultMQPushConsumer.getNamespace()));
        }
    }
```

这里实际走的消息发送逻辑`consumerSendMessageBack`。

`consumerSendMessageBack`就是简单的消息发送，没有复杂的逻辑

![](https://img-blog.csdnimg.cn/img_convert/eb337354ad11dae7eb2d9a4046c4ffb1.png)


我们通过`RequestCode.CONSUMER_SEND_MSG_BACK` 找到broker的处理逻辑

## broker源码分析

处理`RequestCode.CONSUMER_SEND_MSG_BACK`请求的主要是`SendMessageProcessor`



![](https://img-blog.csdnimg.cn/img_convert/dbef3b04138f9c63f95872c8e009e5c8.png)

我们进入到
`org.apache.rocketmq.broker.processor.AbstractSendMessageProcessor#consumerSendMsgBack`方法看看


这里就有延时消息的处理。我们在上面一直说过延时消息的等级一直是0.我们看看是如何处理的


![](https://img-blog.csdnimg.cn/img_convert/6347ea15022a0d946d222dbce63c5ac6.png)

如果重试次数没有超过最大重试次数。并且延时消息等级为0.

则延时消息等级为 **重试次数+3**

这里就清楚知道了为什么消息重试的时间和延时消息是如何相似了吧。

没错 消息重试就是用延时消息实现的 哈哈


然后最大重试次数在哪获取的呢？答案就是`SubscriptionGroupConfig`

也就是订阅信息的元数据

![](https://img-blog.csdnimg.cn/img_convert/21c96418afdddb5ca0c8493797079464.png)

元数据再`broker`的文件就是`subscriptionGroup.json`

格式就是如下
```json
 "GID_xiaozoujishu":{
                        "brokerId":0,
                        "consumeBroadcastEnable":true,
                        "consumeEnable":true,
                        "consumeFromMinEnable":true,
                        "consumeMessageOrderly":false,
                        "consumeTimeoutMinute":15,
                        "groupName":"GID_xiaozoujishu",
                        "groupRetryPolicy":{
                                "type":"CUSTOMIZED"
                        },
                        "groupSysFlag":0,
                        "notifyConsumerIdsChangedEnable":true,
                        "retryMaxTimes":16,
                        "retryQueueNums":1,
                        "whichBrokerWhenConsumeSlowly":1
                }
```


这里可以看到一个细节就是重试策略是`CUSTOMIZED`(CustomizedRetryPolicy)


![](https://img-blog.csdnimg.cn/img_convert/3f12c4e7f6f1bd7759a3fe449023f8d4.png)

也就是我们对应的重试时间

还有一个策略就是`ExponentialRetryPolicy`也就是自定义重试次数

不过最大也就只能指定32次


![](https://img-blog.csdnimg.cn/img_convert/8c09efa1b500f2d510fbaf008cd1e1d0.png)

还有一个细节就是重试次数在哪增加的？实际也是在`org.apache.rocketmq.broker.processor.AbstractSendMessageProcessor#consumerSendMsgBack`这个方法


![](https://img-blog.csdnimg.cn/img_convert/d10f0bb19dce84c68d8a364b3b9f0536.png)

## 总结

普通消息重试默认是16次，顺序消息默认是`Integer.MAX_VALUE`，消息消费失败会以延时消息的方式投递到`broker`，超过延时消息的最大值则以2小时为重试间隔。现在提供了不同的重试策略，默认是`CustomizedRetryPolicy`

如果`broker`投递失败则会在本地再次消费该消息.重试次数的元数据存储在`subscriptionGroup.json`.

重试次数的增加有两种
1. 发送到broker后在broker进行`reconsumeTimes`+1(broker重试)
2. 如果发送`broker`失败则在本地消费进行`reconsumeTimes`+1(client重试)




