package com.ascklrt.infrastructure.framework.netty.im.server.handler;

import com.ascklrt.infrastructure.framework.netty.im.protocol.encode.PacketCodeC;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.request.LoginRequestPacket;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.response.LoginResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 根据解码对象来进行分配Handler
 *
 * SimpleChannelInboundHandler的使用非常简单。我们在继承这个类的时候，给它传递一个泛型参数，
 * 然后在channelRead0()方法里，不用再通过if逻辑来判断当前对象是否是本Handler可以处理的对象，
 * 也不用强转，不用往下传递本Handler处理不了的对象，这一切都已经交给父类
 * SimpleChannelInboundHandler来实现，我们只需要专注于我们要处理的业务逻辑即可。
 */
public class LoginRequestHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket packet) throws Exception {
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
    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
