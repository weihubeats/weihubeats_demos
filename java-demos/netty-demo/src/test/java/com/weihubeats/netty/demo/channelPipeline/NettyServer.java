package com.weihubeats.netty.demo.channelPipeline;

import java.nio.charset.StandardCharsets;

import com.weihubeats.netty.demo.channelPipeline.handler.CustomInboundHandler1;
import com.weihubeats.netty.demo.channelPipeline.handler.CustomInboundHandler2;
import com.weihubeats.netty.demo.channelPipeline.handler.CustomOutboundHandler1;
import com.weihubeats.netty.demo.channelPipeline.handler.CustomOutboundHandler2;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
	public static void main(String[] args) throws InterruptedException {
		// 启动服务器
		startServer();
		
	}

	private static void startServer() throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();


		b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 1024)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new CustomInboundHandler1());
						pipeline.addLast(new CustomInboundHandler2());
						pipeline.addLast(new CustomOutboundHandler1());
						pipeline.addLast(new CustomOutboundHandler2());
						pipeline.addLast(new SimpleChannelInboundHandler<ByteBuf>() {
							@Override
							protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
								//buf.readableBytes()获取缓冲区可读的字节数
								byte[] req = new byte[msg.readableBytes()];
								// 将缓冲区的字节数组复制到新的byte数组中
								msg.readBytes(req);
								String body = new String(req, StandardCharsets.UTF_8);

								System.out.println("Server received: " + body);
								ByteBuf firstMessage;
								byte[] req1 = "你好客户端".getBytes();
								firstMessage = Unpooled.buffer(req1.length);
								firstMessage.writeBytes(req1);
								System.out.println("开始给客户端发送消息");
								ctx.writeAndFlush(firstMessage);
							}
							
							@Override
							public void channelRegistered(ChannelHandlerContext ctx) {
								System.out.println("连接上来了");
								ctx.fireChannelRegistered();
							}
						});
					}
				});

		ChannelFuture future = b.bind(8888).sync();
		future.channel().closeFuture().sync();
	}

	


}
