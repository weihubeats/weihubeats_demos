package com.weihubeats.netty.demo.channelPipeline;


import java.nio.charset.StandardCharsets;

import com.weihubeats.netty.demo.channelPipeline.handler.CustomInboundHandler1;
import com.weihubeats.netty.demo.channelPipeline.handler.CustomInboundHandler2;
import com.weihubeats.netty.demo.channelPipeline.handler.CustomOutboundHandler1;
import com.weihubeats.netty.demo.channelPipeline.handler.CustomOutboundHandler2;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 *@author : wh
 *@date : 2023/11/8 18:21
 *@description:
 */
public class NettyClient {

	public static void main(String[] args) throws Exception {
		// 启动客户端
		startClient();
	}

	public static void startClient() throws InterruptedException {
		Bootstrap clientBootstrap = new Bootstrap();
		NioEventLoopGroup group = new NioEventLoopGroup();

		clientBootstrap.group(group)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new CustomOutboundHandler2());
						pipeline.addLast(new CustomOutboundHandler1());
						pipeline.addLast(new CustomInboundHandler2());
						pipeline.addLast(new CustomInboundHandler1());
						pipeline.addLast(new SimpleChannelInboundHandler<ByteBuf>() {
							@Override
							public void channelActive(ChannelHandlerContext ctx) {
								ByteBuf firstMessage;
								byte[] req = "你好服务器".getBytes();
								firstMessage = Unpooled.buffer(req.length);
								firstMessage.writeBytes(req);
								System.out.println("------------ 开始发送消息 ------------");
								ctx.writeAndFlush(firstMessage);
							}

							@Override
							protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
								//buf.readableBytes()获取缓冲区可读的字节数
								byte[] req = new byte[msg.readableBytes()];
								// 将缓冲区的字节数组复制到新的byte数组中
								msg.readBytes(req);
								String body = new String(req, StandardCharsets.UTF_8);

								System.out.println("Client received: " + body);
							}

						});
					}
				});

		ChannelFuture future = clientBootstrap.connect("localhost", 8888).sync();
		future.channel().closeFuture().sync();
	}
}
