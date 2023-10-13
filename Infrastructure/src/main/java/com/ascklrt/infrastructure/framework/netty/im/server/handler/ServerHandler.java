package com.ascklrt.infrastructure.framework.netty.im.server.handler;

import com.ascklrt.infrastructure.framework.netty.im.protocol.Packet;
import com.ascklrt.infrastructure.framework.netty.im.protocol.PacketCodeC;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.LoginRequestPacket;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.response.LoginResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf) msg;

        // 解码
        Packet packet = PacketCodeC.INSTANCE.decode(buffer);

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

            PacketCodeC.INSTANCE.encode(ctx.alloc().buffer(), loginResponsePacket);
        }


    }


    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
