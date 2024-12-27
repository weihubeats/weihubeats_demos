
1. 下线从节点

```shell
jps java

kill -pid
```

2. 删除相关的元数据

需要删除的元数据
1. 消息存储相关数据
```shell
/data/rmqstore
```

3. 删除主备的版本阶段、纪元等文件

```shell
rm - ～/store/epochFileCheckpoint
rm - ～/store/epochFileCheckpoint.bak
```

4. 从`controller`删除元数据信息
```shell
# 进入倒脚本目录
cd /root/rocketmq/distribution/target/rocketmq-5.1.0/rocketmq-5.1.0
# 查看同步信息 -a controller地址 -b broker名称
sh bin/mqadmin getSyncStateSet -a 127.0.0.1:9878 -b broker-a

# 删除节点元数据 -a controller地址 -b broker名称 -n 节点名称 -c 集群名称
sh bin/mqadmin cleanBrokerData -a 127.0.0.1:9878 -b 127.0.0.3:30911 -n broker-a -c xiaozou
```