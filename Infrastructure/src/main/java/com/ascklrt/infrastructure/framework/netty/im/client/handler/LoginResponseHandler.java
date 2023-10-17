package com.ascklrt.infrastructure.framework.netty.im.client.handler;

import com.ascklrt.infrastructure.framework.netty.im.protocol.command.response.LoginResponsePacket;
import com.ascklrt.infrastructure.framework.netty.im.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket packet) throws Exception {
        if (packet.isSuccess()) {
            LoginUtil.markAsLogin(ctx.channel());
            System.out.println(new Date() + "客户端登陆成功");
        } else {
            System.out.println(new Date() + "客户端登陆失败，原因：" + packet.getReason());
        }
    }
}
