package com.ascklrt.infrastructure.framework.netty.im.client.handler;

import com.ascklrt.infrastructure.framework.netty.im.protocol.command.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket packet) throws Exception {
        System.out.println("服务端回复的消息：" + packet.getMessage());
    }
}
