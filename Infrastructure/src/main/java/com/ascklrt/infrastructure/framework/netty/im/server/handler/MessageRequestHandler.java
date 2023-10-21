package com.ascklrt.infrastructure.framework.netty.im.server.handler;

import com.ascklrt.infrastructure.framework.netty.im.protocol.encode.PacketCodeC;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.request.MessageRequestPacket;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.response.MessageResponsePacket;
import com.ascklrt.infrastructure.framework.netty.im.server.user.Session;
import com.ascklrt.infrastructure.framework.netty.im.server.user.SessionUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket packet) throws Exception {
        System.out.println("收到客户端消息：" + packet.getMessage());

        // 1. 获得消息发送方的会画信息
        Session session = SessionUtil.getSession(ctx.channel());

        // 2. 通过消息发送方的会话信息构造要发送的消息
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setFromUserId(session.getUserId());
        messageResponsePacket.setFromUserName(session.getUserName());
        messageResponsePacket.setMessage("服务端回复：「" + packet.getMessage() + "」");
        ctx.channel().writeAndFlush(messageResponsePacket);

        // ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc().buffer(), messageResponsePacket);
    }
}