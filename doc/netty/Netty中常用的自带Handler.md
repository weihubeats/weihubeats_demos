
# handler生命周期

ChannelRegistered -> ChannelActive -> ChannelRead -> ChannelReadComplete -> ChannelInactive -> ChannelUnregistered

ChannelRegistered -> ChannelActive -> ChannelInactive -> ChannelUnregistered

当ChannelHandler被添加到ChannelPipeline中或者从ChannelPipeline中被删除都会触发这些操作
- handlerAdded 当把 ChannelHandler 添加到 ChannelPipeline 中时被调用
- handlerRemoved 当从 ChannelPipeline 中移除 ChannelHandler 时被调用
- exceptionCaught 当处理过程中在 ChannelPipeline 中有错误产生时被调用

# ChannelInboundHandler方法
- channelRegistered 当 Channel 已经注册到它的 EventLoop 并且能够处理 I/O 时被调用
- channelUnregistered 当 Channel 从它的 EventLoop 注销并且无法处理任何 I/O 时被调用
- channelActive 当 Channel 处于活动状态时被调用；Channel 已经连接/绑定并且已经就绪
- channelInactive 当 Channel 离开活动状态并且不再连接到它的远程节点时被调用
- channelRead 当从 Channel 读取数据时被调用
- channelReadComplete 当 Channel 上的读操作完成时被调用
- userEventTriggered 当 ChannelnboundHandler.fireUserEventTriggered() 方法被调用时被调用
- channelWritabilityChanged 当 Channel 的可写状态发生改变时被调用。用户可以确保写操作不会完成得太快（以避免发生 OutOfMemoryError）或者可以在 Channel 变为再 次可写时恢复写入。可以通过调用 Channel 的 isWritable()方法来检测Channel 的可写性。与可写性相关的阈值可以通过 Channel.config(). setWriteHighWaterMark()和 Channel.config().setWriteLowWaterMark()方法来设置

# ChannelOutboundHandler
- bind 当请求将 Channel 绑定到本地地址时被调用
- connect 当请求将 Channel 连接到远程节点时被调用
- disconnect 当请求断开与远程节点的连接时被调用
- close 当 Channel 关闭时被调用
- deregister 当 Channel 从它的 EventLoop 注销时被调用
- read 当从 Channel 读取数据时被调用
- write 当通过 Channel 写入数据时被调用
- flush 当通过 Channel 写入数据并且完成写入时被调用

# ChannelPipeline

入站事件
- fireChannelRegistered 调用 ChannelPipeline 中下一个 ChannelInboundHandler的channelRegistered(ChannelHandlerContext)方法
- fireChannelUnregistered 调用 ChannelPipeline 中下一个 ChannelInboundHandler的channelUnregistered(ChannelHandlerContext)方法
- fireChannelActive 调用 ChannelPipeline 中下一个 ChannelInboundHandler的channelActive(ChannelHandlerContext)方法
- fireChannelInactive 调用 ChannelPipeline 中下一个 ChannelInboundHandler的channelInactive(ChannelHandlerContext)方法
- fireExceptionCaught 调用 ChannelPipeline 中下一个 ChannelInboundHandler的exceptionCaught(ChannelHandlerContext, Throwable)方法
- fireUserEventTriggered 调用 ChannelPipeline 中下一个 ChannelInboundHandler的userEventTriggered(ChannelHandlerContext, Object)方法
- fireChannelRead 调用 ChannelPipeline 中下一个 ChannelInboundHandler的channelRead(ChannelHandlerContext, Object)方法
- fireChannelReadComplete 调用 ChannelPipeline 中下一个 ChannelInboundHandler的channelReadComplete(ChannelHandlerContext)方法
- fireChannelWritabilityChanged 调用 ChannelPipeline 中下一个 ChannelInboundHandler的channelWritabilityChanged(ChannelHandlerContext)方法


# 自带handler

## SimpleChannelInboundHandler
专门用于处理入站事件的处理器

## SimpleChannelOutboundHandler
专门用于处理出站事件的处理器

## ChannelDuplexHandler
既可以处理入站事件，也可以处理出站事件

