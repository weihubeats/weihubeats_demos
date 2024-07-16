## socket

- SocketChannel 对应 tcp/ip
```java
SocketChannel channel = SocketChannel.open( );
channel.connect(new InetSocketAddress("localhost", portNumber));

```

- DatagramChannel 对应 udp/ip
```java
DatagramChannel channel = DatagramChannel.open( );
DatagramSocket socket = channel.socket( );
socket.bind (new InetSocketAddress (portNumber));
```

## 选择器可注册的事件
- SelectionKey.OP_ACCEPT 接受
- SelectionKey.OP_CONNECT 连接
- SelectionKey.OP_READ 读
- SelectionKey.OP_WRITE 写

SocketChannel 不支持OP_ACCEPT