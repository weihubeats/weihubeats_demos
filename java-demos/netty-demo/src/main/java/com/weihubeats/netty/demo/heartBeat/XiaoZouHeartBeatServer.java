package com.weihubeats.netty.demo.heartBeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author : wh
 * @date : 2024/6/28 17:37
 * @description:
 */
public class XiaoZouHeartBeatServer {

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            NettyConnectManageHandler manageHandler = new NettyConnectManageHandler();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline()
                        .addLast(StringDecoder.class.getSimpleName(), new StringDecoder())
                        .addLast(StringEncoder.class.getSimpleName(), new StringEncoder())
                        .addLast(new IdleStateHandler(0, 0, 10))
                        .addLast(manageHandler)
                        .addLast(new ReadMsgHandler());

                }
            });
            ChannelFuture future = bootstrap.bind(9000).sync();
            System.out.println("xiaozou netty server start done.");
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}


