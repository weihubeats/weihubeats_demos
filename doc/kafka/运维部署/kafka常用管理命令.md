
## 删除consumer(gid)

```shell
cd /home/ubuntu/kafka_2.13-3.5.0/bin

./kafka-consumer-groups.sh --bootstrap-server kafka-001.com:9092, kafka-002.com:9092, kafka-003.com:9092 --delete --group xiaozou
```


## 查看topic磁盘占用情况

```shell
./kafka-log-dirs.sh --bootstrap-server kafka-001.com:9092, kafka-002.com:9092, kafka-003.com:9092 --describe --topic-list xiaozou-topic
```