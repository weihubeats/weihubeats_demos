## 背景

目前`client`在启动后会定时向所有`broker`发送心跳,通过心跳数据以告知`broker`该客户端可正常工作。同时保证channel的不会因为心跳异常而被关闭


```java
        this.scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                MQClientInstance.this.cleanOfflineBroker();
                MQClientInstance.this.sendHeartbeatToAllBrokerWithLock();
            } catch (Exception e) {
                log.error("ScheduledTask sendHeartbeatToAllBroker exception", e);
            }
        }, 1000, this.clientConfig.getHeartbeatBrokerInterval(), TimeUnit.MILLISECONDS);
```

心跳间隔默认30s一次

```java
private int heartbeatBrokerInterval = 1000 * 30;

```



但是目前`client`每次发送的心跳数据都包含括该客户端下的所有消费者的订阅数据，这在每个`consumerGroup`都具有相同的订阅情况下，数据和计算具有一定的冗余性


## 优化心跳 [RIP-64] Heartbeat Optimization


所以在`RocketMQ` [RIP-64]提出了对心跳机制进行优化，推出了`useHeartbeatV2`



### 优化方向

既然是订阅数据不变的`consumerGroup`发送客户端所有的消费中的订阅数据太冗余，那么我们就可以进行如下优化:

在平时的心跳发送过程中不用发送全量完整的心跳数据，只需要简单发送订阅数据的指纹，从而减少客户端向broker传输的心跳数据以及broker对于心跳的重复计算


### 源码分析

1. 首先在`ClientConfig`中新增配置`useHeartbeatV2`，表示是否开启v2版本的心跳，默认`false`

```java
public static final String HEART_BEAT_V2 = "com.rocketmq.heartbeat.v2";

private boolean useHeartbeatV2 = Boolean.parseBoolean(System.getProperty(HEART_BEAT_V2, "false"));
```

可以通过系统参数进行设置开启

```java
System.setProperty(ClientConfig.HEART_BEAT_V2, "true");
```


在原先的`sendHeartbeatToAllBrokerWithLock`方法中会进行判断是否开启V2版本的心跳机制

```java
    public boolean sendHeartbeatToAllBrokerWithLock() {
        if (this.lockHeartbeat.tryLock()) {
            try {
                if (clientConfig.isUseHeartbeatV2()) {
                    return this.sendHeartbeatToAllBrokerV2(false);
                } else {
                    return this.sendHeartbeatToAllBroker();
                }
            } catch (final Exception e) {
                log.error("sendHeartbeatToAllBroker exception", e);
            } finally {
                this.lockHeartbeat.unlock();
            }
        } else {
            log.warn("lock heartBeat, but failed. [{}]", this.clientId);
        }
        return false;
    }
```

#### sendHeartbeatToAllBrokerV2

我们来看看v2版本的心跳机制主要做什么，和V1有什么区别




```java
sendHeartbeatToAllBrokerWithLockV2
```


## 参考

- [RIP-64-doc](https://docs.google.com/document/d/174p0N7yDX0p_jvaujwHGFsZ410FKbq3vqGT1RUUyV1c/edit?tab=t.0)
- [RIP-64-pr](https://github.com/apache/rocketmq/pull/6724)