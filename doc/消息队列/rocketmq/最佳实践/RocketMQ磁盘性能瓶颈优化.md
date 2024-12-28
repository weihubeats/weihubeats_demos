## 背景

线上发送消息有少量的`TimeOut`


## 排查

经过各方面排查，查看`store.log`发现`PAGECACHERT`写入的性能并不是很好

```java
2024-11-09 13:20:08 INFO StoreStatsService - [PAGECACHERT] TotalPut xxx, PutMessageDistributeTime [<=0ms]:xxx [0~10ms]:xxx [10~50ms]:xxx [50~100ms]:xxx [100~200ms]:xxx [200~500ms]:xxx [500ms~1s]:0 [1~2s]:0 [2~3s]:0 [3~4s]:0 [4~5s]:0 [5~10s]:0 [10s~]:0 
```


大概的请求占比如下:

`<=0ms`: `22.32%`

`0~10ms`: `65.66%`

`10~50ms`: `8.92%`

`50~100ms`: `0.88%`

`100~200ms`: `2.02%`

`200~500ms`: `0.19%`


有约 2% 的操作在 100~200ms 区间，这个比例略高,其次200~500ms 区间虽然比例很低，但是也对线上服务有影响，需要优化


## 优化方向

```shell
cat /proc/sys/vm/dirty_ratio
cat /proc/sys/vm/dirty_background_ratio

echo "vm.dirty_ratio = 30" >> /etc/sysctl.conf
echo "vm.dirty_background_ratio = 10" >> /etc/sysctl.conf
sudo sysctl -p
```

主要是调整`dirty_ratio`和`dirty_background_ratio`的值，这两个参数是关于脏页的

- dirty_ratio：系统内存中可以存放的"脏页"的最大百分比,当达到这个阈值时，系统会强制进行磁盘写入（同步写入），所有 I/O 将会阻塞

- dirty_background_ratio: 当达到这个阈值时，系统会强制进行磁盘写入（同步写入），所有 I/O 将会阻塞.当达到这个阈值时，系统会开始在后台将脏页写入磁盘


1. 内存使用效率低下：
    - 允许累积太多脏页，占用大量内存
    - 可能导致其他进程内存不足
2. I/O 突发：
    - 当达到 50% 时，会突然产生大量 I/O
    - 可能导致系统响应变慢或卡顿
3. 写入延迟：
    - 脏页积累太多才开始写入
    - 可能导致数据丢失风险增加