package com.weihubeats.netty.demo.heartBeat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import java.util.Objects;

/**
 * @author : wh
 * @date : 2024/6/28 20:16
 * @description:
 */
public class ReadMsgHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) {
        if (Objects.equals(s, HeartBeatConstant.HEART_BEAT)) {
            System.out.println("[心跳消息]");
            ctx.channel().writeAndFlush("ok");
        } else {
            System.out.println("[非心跳消息]");
        }
        System.out.println(" [xiaozou] [server] message received : " + s);
    }

}
