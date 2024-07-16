## @ChannelHandler.Sharable作用

实际是一个标记注解，用于标识一个`ChannelHandler`实例可以被添加到多个`ChannelPipeline`中，而不会引发竞态条件（race condition）。

仅此而已，至于一个`ChannelHandler`是否单例和`@ChannelHandler.Sharable`注解没有什么关系

不过当netty尝试往多个`channel`的`pipeline`中添加同一个`ChannelHandlerAdapter`实例时，会判断该实例类是否添加了`@Sharable`，没有则抛出`is not a @Sharable handler, so can't be added or removed multiple times`异常


原文链接：https://blog.csdn.net/wb_snail/article/details/106263470

## 什么时候需要添加@ChannelHandler.Sharable注解

1. 无状态处理器：如果你的处理器不维护任何状态（例如成员变量），那么它可以被多个管道共享。在这种情况下，你可以使用@Sharable注解。例如，一个简单的日志处理器：
```java
@ChannelHandler.Sharable
public class LoggingHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Received message: " + msg);
        ctx.fireChannelRead(msg);
    }
}
```
这个LoggingHandler没有任何状态，因此可以安全地在多个管道中共享

2. 共享资源的处理器：如果你的处理器需要访问共享资源（例如数据库连接池），并且这些资源是线程安全的，那么你也可以使用@Sharable注解。例如：
```java
@ChannelHandler.Sharable
public class DatabaseHandler extends ChannelInboundHandlerAdapter {
    
    private final DataSource dataSource;

    public DatabaseHandler(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 使用dataSource进行数据库操作
        ctx.fireChannelRead(msg);
    }
}
```
这里的DatabaseHandler使用了一个线程安全的DataSource，因此可以在多个管道中共享。

## 什么时候不需要加@ChannelHandler.Sharable注解
1. 有状态处理器：如果你的处理器维护了某种状态（例如成员变量），那么它不应该被多个管道共享。例如：

```java

public class StatefulHandler extends ChannelInboundHandlerAdapter {
    private int counter = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        counter++;
        System.out.println("Message count: " + counter);
        ctx.fireChannelRead(msg);
    }
}
```

这个StatefulHandler维护了一个计数器，因此每个连接都需要一个新的实例，以避免状态冲突

2. 非线程安全资源：如果你的处理器使用了非线程安全的资源，那么它也不应该被多个管道共享。例如：
```java
public class NonThreadSafeHandler extends ChannelInboundHandlerAdapter {
    private final SomeNonThreadSafeResource resource;

    public NonThreadSafeHandler(SomeNonThreadSafeResource resource) {
        this.resource = resource;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 使用resource进行操作
        ctx.fireChannelRead(msg);
    }
}
```
这里的NonThreadSafeHandler使用了一个非线程安全的资源，因此每个连接都需要一个新的实例

总结来说，@ChannelHandler.Sharable注解适用于无状态或使用线程安全资源的处理器，而有状态或使用非线程安全资源的处理器则不应使用这个注解。

