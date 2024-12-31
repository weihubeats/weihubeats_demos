## 背景

线上的`Kafka`集群采用的部署方式为`KRaft`模式，3个`KRaft`+2个`broker`。`Kafka`版本`kafka_2.13-3.5.0`

由于一些原因要下线掉旧的`broker`新增三个`broker`

## 如何扩容
将新服务器添加到`Kafka`集群很容易，只需为它们分配一个唯一的`brokerId` 即可，新的`broker`就会自动加入到`Kafka`集群中

不过这些新服务器不会自动分配任何数据分区，也就是原先的`topic`的数据不会存在新集群，也不会分担原来`topic`的请求

如果要旧集群的`topic`在新集群添加分区数据，就需要手动迁移。

分区迁移`Kafka`提供了`kafka-reassign-partitions.sh`脚本，数据迁移过程是手动，但是完全自动化

## 实战

### 1. 优化linux内核参数

```shell
sh os.sh
```

os的详细脚本如下
```shell
#!/bin/bash

# 确保脚本以root权限运行
if [ "$(id -u)" != "0" ]; then
   echo "该脚本必须以root权限运行" 1>&2
   exit 1
fi

# 更新文件描述符限制
echo "* soft nofile 100000" >> /etc/security/limits.conf
echo "* hard nofile 100000" >> /etc/security/limits.conf

# 更新虚拟内存设置
sysctl -w vm.swappiness=1
echo "vm.swappiness=1" >> /etc/sysctl.conf

sysctl -w vm.max_map_count=262144
echo "vm.max_map_count=262144" >> /etc/sysctl.conf

# 更新网络设置
sysctl -w net.ipv4.tcp_fin_timeout=30
sysctl -w net.ipv4.tcp_tw_reuse=1
sysctl -w net.core.rmem_max=16777216
sysctl -w net.core.wmem_max=16777216
sysctl -w net.ipv4.tcp_max_syn_backlog=8096
sysctl -w net.core.somaxconn=8192
sysctl -w net.core.netdev_max_backlog=4096
sysctl -w net.ipv4.tcp_rmem='4096 87380 16777216'
sysctl -w net.ipv4.tcp_wmem='4096 65536 16777216'
sysctl -w net.ipv4.tcp_keepalive_intvl=30
sysctl -w net.ipv4.tcp_keepalive_probes=5
sysctl -w net.ipv4.tcp_keepalive_time=120

echo "net.ipv4.tcp_fin_timeout=30" >> /etc/sysctl.conf
echo "net.ipv4.tcp_tw_reuse=1" >> /etc/sysctl.conf
echo "net.core.rmem_max=16777216" >> /etc/sysctl.conf
echo "net.core.wmem_max=16777216" >> /etc/sysctl.conf
echo "net.ipv4.tcp_max_syn_backlog=8096" >> /etc/sysctl.conf
echo "net.core.somaxconn=8192" >> /etc/sysctl.conf
echo "net.core.netdev_max_backlog=4096" >> /etc/sysctl.conf
echo "net.ipv4.tcp_rmem=4096 87380 16777216" >> /etc/sysctl.conf
echo "net.ipv4.tcp_wmem=4096 65536 16777216" >> /etc/sysctl.conf
echo "net.ipv4.tcp_keepalive_intvl=30" >> /etc/sysctl.conf
echo "net.ipv4.tcp_keepalive_probes=5" >> /etc/sysctl.conf
echo "net.ipv4.tcp_keepalive_time=120" >> /etc/sysctl.conf

# 更新最大线程数限制
echo "* soft nproc 4096" >> /etc/security/limits.d/90-nproc.conf
echo "* hard nproc 4096" >> /etc/security/limits.d/90-nproc.conf

# 应用sysctl设置
sysctl -p

echo "系统调优完成。可能需要重启以应用某些更改。"
```

### 2. 安装jdk11
```shell

### 1. 安装jdk11

```shell
sudo apt update
apt install openjdk-11-jdk -y
```
### 3. 下载kafka二进制包
```shell
wget http://mirrors.aliyun.com/apache/kafka/3.5.0/kafka_2.13-3.5.0.tgz
```

### 4. 解压
```shell
tar -xzf kafka_2.13-3.5.0.tgz
```

### 5. 修改`broker`配置文件

主要修改参数
- node.id 唯一，不能与其他broker相同
- log.dirs Kafka日志（消息）的存储路径
- Listeners: 配置Broker的监听地址，用于客户端连接
- log.dirs需要修改成多块磁盘的路径

```shell
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

#
# This configuration file is intended for use in KRaft mode, where
# Apache ZooKeeper is not present.
#

############################# Server Basics #############################

# The role of this server. Setting this puts us in KRaft mode
process.roles=broker

# The node id associated with this instance's roles
node.id=6

# The connect string for the controller quorum
controller.quorum.voters=1@kafka-controller-001.xiaozou.com:9093,2@kafka-controller-002.xiaozou.com:9093,3@kafka-controller-003.xiaozou.com:9093

############################# Socket Server Settings #############################

# The address the socket server listens on. If not configured, the host name will be equal to the value of
# java.net.InetAddress.getCanonicalHostName(), with PLAINTEXT listener name, and port 9092.
#   FORMAT:
#     listeners = listener_name://host_name:port
#   EXAMPLE:
#     listeners = PLAINTEXT://your.host.name:9092
listeners=PLAINTEXT://kafka-002.xiaozou.com:9092

# Name of listener used for communication between brokers.
inter.broker.listener.name=PLAINTEXT


# A comma-separated list of the names of the listeners used by the controller.
# This is required if running in KRaft mode. On a node with `process.roles=broker`, only the first listed listener will be used by the broker.
controller.listener.names=CONTROLLER

# Maps listener names to security protocols, the default is for them to be the same. See the config documentation for more details
listener.security.protocol.map=CONTROLLER:PLAINTEXT,PLAINTEXT:PLAINTEXT,SSL:SSL,SASL_PLAINTEXT:SASL_PLAINTEXT,SASL_SSL:SASL_SSL

# The number of threads that the server uses for receiving requests from the network and sending responses to the network
num.network.threads=3

# The number of threads that the server uses for processing requests, which may include disk I/O
num.io.threads=8

# The send buffer (SO_SNDBUF) used by the socket server
socket.send.buffer.bytes=102400

# The receive buffer (SO_RCVBUF) used by the socket server
socket.receive.buffer.bytes=102400

# The maximum size of a request that the socket server will accept (protection against OOM)
socket.request.max.bytes=104857600


############################# Log Basics #############################

# A comma separated list of directories under which to store log files
log.dirs=/data/kraft-broker-logs,/data1/kraft-broker-logs,/data2/kraft-broker-logs

# The default number of log partitions per topic. More partitions allow greater
# parallelism for consumption, but this will also result in more files across
# the brokers.
num.partitions=2

# The number of threads per data directory to be used for log recovery at startup and flushing at shutdown.
# This value is recommended to be increased for installations with data dirs located in RAID array.
num.recovery.threads.per.data.dir=1

############################# Internal Topic Settings  #############################
# The replication factor for the group metadata internal topics "__consumer_offsets" and "__transaction_state"
# For anything other than development testing, a value greater than 1 is recommended to ensure availability such as 3.
offsets.topic.replication.factor=2
transaction.state.log.replication.factor=2
transaction.state.log.min.isr=2

############################# Log Flush Policy #############################

# Messages are immediately written to the filesystem but by default we only fsync() to sync
# the OS cache lazily. The following configurations control the flush of data to disk.
# There are a few important trade-offs here:
#    1. Durability: Unflushed data may be lost if you are not using replication.
#    2. Latency: Very large flush intervals may lead to latency spikes when the flush does occur as there will be a lot of data to flush.
#    3. Throughput: The flush is generally the most expensive operation, and a small flush interval may lead to excessive seeks.
# The settings below allow one to configure the flush policy to flush data after a period of time or
# every N messages (or both). This can be done globally and overridden on a per-topic basis.

# The number of messages to accept before forcing a flush of data to disk
#log.flush.interval.messages=10000

# The maximum amount of time a message can sit in a log before we force a flush
#log.flush.interval.ms=1000

############################# Log Retention Policy #############################

# The following configurations control the disposal of log segments. The policy can
# be set to delete segments after a period of time, or after a given size has accumulated.
# A segment will be deleted whenever *either* of these criteria are met. Deletion always happens
# from the end of the log.

# The minimum age of a log file to be eligible for deletion due to age
log.retention.hours=168

# A size-based retention policy for logs. Segments are pruned from the log unless the remaining
# segments drop below log.retention.bytes. Functions independently of log.retention.hours.
#log.retention.bytes=1073741824

# The maximum size of a log segment file. When this size is reached a new log segment will be created.
log.segment.bytes=1073741824

# The interval at which log segments are checked to see if they can be deleted according
# to the retention policies
log.retention.check.interval.ms=300000

auto.create.topics.enable=false
message.max.bytes=10485760
```

### 6. 格式化broker
```shell
sh /home/ubuntu/kafka_2.13-3.5.0/bin/kafka-storage.sh format -t mzDehCx0RNmxa27PRMpNkB  -c /home/ubuntu/kafka_2.13-3.5.0/config/kraft/broker.properties
```

注意这里的集群id是之前生成的。如果不记得可以用如下方式查询
1. 进入到kafka log文件目录
```shell
cd /tmp/kraft-broker-logs
```

2. 找到`meta.properties`文件，然后查看
```shell
vim meta.properties
```
输出集群的元数据信息如下
```properties
node.id=5
version=1
cluster.id=mzDehCx0RNmxa27PRMpNkB
```

### 7. 启动broker
```shell
export KAFKA_HEAP_OPTS="-Xmx6G -Xms6G"&&JMX_PORT=9988 nohup sh /data/kafka_2.13-3.5.0/bin/kafka-server-start.sh /data/kafka_2.13-3.5.0/config/kraft/broker.properties &
```

> 这里的JVM内存给6G基本就够了，Kafka主要使用的是堆外内存


### 8. 旧节点下线

这里由于线上的业务允许一定时间不可用。所以可以直接进行下线旧`broker`，不需要进行分区迁移，然后重新创建`topic`即可

> 最好的无损方式还是先进行分区迁移，然后再下线旧`broker`

需要注意如果有旧节点下线可能会出现消息发送正常，消息消费失败的情况，原因可能是`__consumer_offsets`这个`topic`没有自动进行分区迁移，仅在旧`broker`存在