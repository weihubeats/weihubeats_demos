> 这里是**weihubeats**,觉得文章不错可以关注公众号**小奏技术**


## 背景

早期在使用`RocketMQ`的时候，系统和开发人员不算多。所以topic的创建会非常随意，各种千奇百怪的`topic`

比如: `order_topic`、`ORDER_TOPIC`、`order-topic`

各种奇奇怪怪的风格，用_的，用驼峰的，纯大写加下划线的。各种风格

其实这个还不是主要的，比如上面的`order_topic`,你大致知道这个`topic`是属于订单组的，比如如果我们出现了一个`domain_event`topic，这时候我们去看这个`topic`，他是属于哪个系统的呢？实际会比较困惑

包括比如要消费这个`topic`，我们可以有如下gid
gid_domian_event

我看了这个gid也不知道这个gid属于哪个系统的，是属于自己系统消费自己的topic，还是属于其他系统消费`domian_event`这个`topic`

所以可以看到如果没有一个`topic`和`gid`创建规范，有时候这些`topic`和`gid`会非常没有意义，在做`topic`或者`gid`维护的时候看这些名字是非常没有意义的

## Topic规范

topic的创建规范我们推荐是
`serviceName` + `topicName`(业务名称)

举个🌰

比如我们有订单系统(order)要创建自己的领域事件topic

则`topic`名称为:`order-domain-event-topic`

## 消费者(gid)规范

规范：`gid` + `消费系统` + `consume` + `topic`

如果是自己消费自己则省略 `消费系统` + `consume`变成

`gid` + `topic`

举个🌰

比如我们支付要消费订单的topic
那么我们的gid为

`gid-pay-consume-order-domain-event-topic`

如果订单要消费自己的`topic`呢？

那我们就可以省略 消费系统 + consume，直接是gid+topic

比如 `gid-order-domain-event-topic`

一些错误的gid命名例子

## 收益

这样我们看到每个`topic`和`gid`就知道是干嘛的了

比如给你一个`order-domain-event-topic`

你就知道是订单组的领域事件`topic`

看到`gid-order-domain-event-topic` 就是订单组自己消费自己的`order-domain-event-topic`

看到`gid-pay-consume-order-domain-event-topic`就知道是支付用来消费订单的`order-domain-event-topic` topic

当然如果公司有足够的研发资源还是可以自研`RocketMQ` 的dashboard，从流程上去规范`topic`的创建，比如创建`topic`的时候需要说明属于哪个系统，用途等

不知道其他小伙伴有没有什么更好的建议呢