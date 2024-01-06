package com.weihubeats.netty.demo.channelPipeline.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 *@author : wh
 *@date : 2023/11/9 09:50
 *@description:
 */
public class CustomOutboundHandler2 extends ChannelOutboundHandlerAdapter {
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		System.out.println("CustomOutboundHandler2 - write");
		super.write(ctx, msg, promise);
	}
}
