package com.ascklrt.infrastructure.framework.netty.im.server.handler;

import com.ascklrt.infrastructure.framework.netty.im.protocol.encode.PacketCodeC;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.request.MessageRequestPacket;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.response.MessageResponsePacket;
import com.ascklrt.infrastructure.framework.netty.im.server.user.Session;
import com.ascklrt.infrastructure.framework.netty.im.server.user.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Objects;

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

        // 3. 发送消息给对应客户端
        Channel toUserChannel = SessionUtil.getChannel(packet.getToUserId());
        if (Objects.nonNull(toUserChannel) && SessionUtil.hasLogin(toUserChannel)) {
            toUserChannel.writeAndFlush(messageResponsePacket);
        } else {
            System.out.println("[" + packet.getToUserId() +  "] 不在线，发送失败！");
        }

                // .writeAndFlush(messageResponsePacket);
        // ctx.channel().writeAndFlush(messageResponsePacket);
        // ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc().buffer(), messageResponsePacket);
    }
}