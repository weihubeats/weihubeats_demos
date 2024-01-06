package com.weihubeats.netty.demo.channelPipeline.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 *@author : wh
 *@date : 2023/11/9 09:48
 *@description:
 */
public class CustomInboundHandler1 extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("CustomInboundHandler1 - channelRead");
		super.channelRead(ctx, msg);
	}
}