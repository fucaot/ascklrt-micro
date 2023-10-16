package com.ascklrt.infrastructure.framework.netty.im.server.handler;

import com.ascklrt.infrastructure.framework.netty.im.protocol.Packet;
import com.ascklrt.infrastructure.framework.netty.im.protocol.PacketCodeC;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.request.LoginRequestPacket;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.request.MessageRequestPacket;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.response.LoginResponsePacket;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.response.MessageResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf) msg;

        // 解码
        Packet packet = PacketCodeC.INSTANCE.decode(buffer);

        // 登陆流程
        if (packet instanceof LoginRequestPacket) {
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            if (valid(loginRequestPacket)) {
                System.out.println("IM-登陆成功！");
                loginResponsePacket.setSuccess(true);
            } else {
                System.out.println("IM-登陆失败！");
                loginResponsePacket.setReason("登陆失败");
                loginResponsePacket.setSuccess(false);
            }

            ByteBuf encode = PacketCodeC.INSTANCE.encode(ctx.alloc().buffer(), loginResponsePacket);
            ctx.channel().writeAndFlush(encode);
        }

        // 发送消息流程
        if (packet instanceof MessageRequestPacket) {
            MessageRequestPacket messageRequestPacket = (MessageRequestPacket) packet;

            System.out.println("收到客户端消息：" + messageRequestPacket.getMessage());

            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            messageResponsePacket.setMessage("服务端回复：「" + messageRequestPacket.getMessage() + "」");
            ByteBuf responseByteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc().buffer(), messageResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }
    }


    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
