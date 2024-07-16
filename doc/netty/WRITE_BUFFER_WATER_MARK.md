## WRITE_BUFFER_WATER_MARK

```java
private void addCustomConfig(ServerBootstrap childHandler) {
    childHandler.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK, new WriteBufferWaterMark(
        nettyServerConfig.getWriteBufferLowWaterMark(), nettyServerConfig.getWriteBufferHighWaterMark()));
    
}
```

## 什么是WRITE_BUFFER_WATER_MARK

- 低水位（Low Water Mark）：当Netty的写缓冲区中的数据量降到低于这个标记时，Channel会变成可写状态（isWritable()返回true）。
- 高水位（High Water Mark）：当Netty的写缓冲区中的数据量超过这个标记时，Channel会变成不可写状态（isWritable()返回false），直到缓冲区中的数据量降到低于低水位标记。

## 如何设置WRITE_BUFFER_WATER_MARK
设置WRITE_BUFFER_WATER_MARK的目的是为了防止在高负载情况下因为缓冲区数据过多而导致的内存溢出，同时也可以通过控制写入速率来对接收方进行一定程度的流量控制。
没有一个通用的值适用于所有情况，通常需要根据具体场景进行调整。然而，有一些指导原则可以帮助你决定起始点：
初始设置：可以从默认值开始调整，Netty的默认高水位是64KB，低水位是32KB。根据你的应用需求，你可以从这个基础上进行调整。
考虑应用需求：如果你的应用需要高吞吐量且可以接受较大的内存使用，可以适当增加高水位的值。反之，如果需要限制内存使用，应适当降低。
监控与调整：最重要的是监控你的应用表现，包括延迟、吞吐量以及内存使用情况。根据监控结果调整WRITE_BUFFER_WATER_MARK的值。如果发现缓冲区频繁达到高水位标记，可能需要增加高水位；如果发现内存使用过高，可能需要降低