package com.weihubeats.netty.demo.heartBeat;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author : wh
 * @date : 2024/6/28 17:42
 * @description:
 */
public class NettyConnectManageHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.ALL_IDLE)) {
                System.out.println("xiaozou 触发 读写空闲事件");
                ctx.writeAndFlush("Closing connection due to idle timeout...");
            }
            System.out.println("开始关闭channel");
            ctx.channel().close();
            System.out.println("关闭channel");
            ctx.fireUserEventTriggered(evt);
        } else {
            super.userEventTriggered(ctx, evt);
        }

    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        System.out.println("关闭channel");
        super.close(ctx, promise);
    }
}
