package com.ascklrt.infrastructure.framework.netty.im.server.handler;

import com.ascklrt.infrastructure.framework.netty.im.protocol.encode.PacketCodeC;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.request.MessageRequestPacket;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.response.MessageResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket packet) throws Exception {
        MessageRequestPacket messageRequestPacket = (MessageRequestPacket) packet;

        System.out.println("收到客户端消息：" + messageRequestPacket.getMessage());

        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setMessage("服务端回复：「" + messageRequestPacket.getMessage() + "」");
        ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc().buffer(), messageResponsePacket);
        ctx.channel().writeAndFlush(responseByteBuf);
    }
}
