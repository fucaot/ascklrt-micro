package com.ascklrt.infrastructure.framework.netty.im.server.handler;

import com.ascklrt.infrastructure.framework.netty.im.protocol.command.request.LoginRequestPacket;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.response.LoginResponsePacket;
import com.ascklrt.infrastructure.framework.netty.im.server.user.Session;
import com.ascklrt.infrastructure.framework.netty.im.server.user.SessionUtil;
import com.ascklrt.infrastructure.framework.netty.im.util.LoginUtil;
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

    private int randomId = 0;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket packet) throws Exception {
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        if (valid(packet)) {
            System.out.println("IM-登陆成功！");

            // 标记为已登陆
            LoginUtil.markAsLogin(ctx.channel());

            // String userId = randomUserId();
            loginResponsePacket.setUserId(packet.getUserId());
            loginResponsePacket.setSuccess(true);

            SessionUtil.bindSession(new Session(packet.getUserId(), packet.getUsername()), ctx.channel());
        } else {
            System.out.println("IM-登陆失败！");
            loginResponsePacket.setReason("登陆失败");
            loginResponsePacket.setSuccess(false);
        }

        // 直接写入Java对象，在pipline中加入PacketEncode，会到PacketEncode进行写入ByteBuf处理
        ctx.channel().writeAndFlush(loginResponsePacket);
        // ByteBuf encode = PacketCodeC.INSTANCE.encode(ctx.alloc().buffer(), loginResponsePacket);
    }
    synchronized private String randomUserId() {
        randomId = randomId + 1;
        return String.valueOf(randomId);
    }
    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
