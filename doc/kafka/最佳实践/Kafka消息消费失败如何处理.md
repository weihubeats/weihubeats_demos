

## kafka消费失败

kafka再拉取消息消费失败后，分两种情况，主要是看消费位点的提交方式

1. 手动提交消费位点
2. 自动提交消费位点

## 自动提交消费位点


如果我们设置了自己提交消费位点即

```java

Properties props = new Properties();
props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
// 自动提交消费位点的时间间隔
props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 5000); 
```