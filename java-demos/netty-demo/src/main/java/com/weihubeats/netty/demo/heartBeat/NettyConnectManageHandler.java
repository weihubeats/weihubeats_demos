package com.weihubeats.netty.demo.heartBeat;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author : wh
 * @date : 2024/6/28 17:42
 * @description:
 */
@ChannelHandler.Sharable
public class NettyConnectManageHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.ALL_IDLE)) {
                System.out.println("[xiaozou] 触发 读写空闲事件");
                ctx.writeAndFlush("Closing connection due to idle timeout...")
                    .addListener((ChannelFutureListener) future -> {
                        if (future.isSuccess()) {
                            System.out.println("[xiaozou] 消息发送成功，关闭连接");
                            future.channel().close();
                        } else {
                            System.out.println("[xiaozou] 消息发送失败");
                            future.cause().printStackTrace();
                        }
                    });
            }
        }
        ctx.fireUserEventTriggered(evt);

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[xiaozou] 连接断开");
        super.channelInactive(ctx);
        ctx.channel().close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[xiaozou] 连接建立");
        super.channelActive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[xiaozou] 连接取消注册");
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[xiaozou] 连接注册");
        super.channelRegistered(ctx);
    }

}
