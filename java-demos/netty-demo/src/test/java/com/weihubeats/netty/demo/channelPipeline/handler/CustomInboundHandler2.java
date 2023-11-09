package com.weihubeats.netty.demo.channelPipeline.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 *@author : wh
 *@date : 2023/11/9 09:49
 *@description:
 */
public class CustomInboundHandler2 extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("CustomInboundHandler2 - channelRead");
		super.channelRead(ctx, msg);
	}
}