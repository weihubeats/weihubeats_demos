package com.weihubeats.netty.demo.heartBeat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author : wh
 * @date : 2024/6/28 19:34
 * @description:
 */
public class XiaoZouHeartBeatClient {

    public static void main(String[] args) throws Exception {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            NettyConnectManageHandler handler = new NettyConnectManageHandler();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline()
                            .addLast(StringDecoder.class.getSimpleName(), new StringDecoder())
                            .addLast(StringEncoder.class.getSimpleName(), new StringEncoder())
                            .addLast(new IdleStateHandler(0, 0, 5))
                            .addLast(handler)
                            .addLast(new ReadMsgHandler());
                    }
                });
            Channel channel = bootstrap.connect("127.0.0.1", 9000).sync().channel();
            System.out.println("xiaozou netty client start done.");
            Random random = new Random();
            while (channel.isActive()) {
                int i = random.nextInt(8);
                System.out.println("random value is " + i);
                TimeUnit.SECONDS.sleep(i);
                channel.writeAndFlush(HeartBeatConstant.HEART_BEAT);
            }

        } finally {
            eventLoopGroup.shutdownGracefully();
        }

    }
}
