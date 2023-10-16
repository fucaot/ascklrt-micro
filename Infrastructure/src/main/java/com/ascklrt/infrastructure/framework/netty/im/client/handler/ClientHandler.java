package com.ascklrt.infrastructure.framework.netty.im.client.handler;

import com.ascklrt.infrastructure.framework.netty.im.protocol.Packet;
import com.ascklrt.infrastructure.framework.netty.im.protocol.encode.PacketCodeC;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.request.LoginRequestPacket;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.response.LoginResponsePacket;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.response.MessageResponsePacket;
import com.ascklrt.infrastructure.framework.netty.im.util.LoginUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.UUID;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + "客户端开始登陆");

        // 创建登陆对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("yl");
        loginRequestPacket.setPassword("pwd");

        // 编码
        ByteBuf buffer = PacketCodeC.INSTANCE.encode(ctx.alloc().buffer(), loginRequestPacket);

        // 写数据
        ctx.channel().writeAndFlush(buffer);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf) msg;

        Packet packet = PacketCodeC.INSTANCE.decode(buffer);
        // 登陆回复
        if (packet instanceof LoginResponsePacket) {
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;

            if (loginResponsePacket.isSuccess()) {
                LoginUtil.markAsLogin(ctx.channel());
                System.out.println(new Date() + "客户端登陆成功");
            } else {
                System.out.println(new Date() + "客户端登陆失败，原因：" + loginResponsePacket.getReason());
            }
        }

        // 消息回复
        if (packet instanceof MessageResponsePacket) {
            MessageResponsePacket messageResponsePacket = (MessageResponsePacket) packet;
            System.out.println("服务端回复的消息：" + messageResponsePacket.getMessage());
        }
    }
}
