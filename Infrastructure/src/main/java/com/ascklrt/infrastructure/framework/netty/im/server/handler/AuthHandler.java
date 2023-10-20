package com.ascklrt.infrastructure.framework.netty.im.server.handler;

import com.ascklrt.infrastructure.framework.netty.im.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 验证登录状态，若没有登陆，则断开连接（断开连接后后续数据包不进行接收）
 */
public class AuthHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!LoginUtil.hasLogin(ctx.channel())) {
            ctx.channel().close();
        } else {
            // 已经验证通过，利用热插拔，将本handler从本次连接的pipline中删除，之后的请求无需再次走验证逻辑
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if (LoginUtil.hasLogin(ctx.channel())) {
            System.out.println("当前连接登陆验证完毕，无须再次验证，AuthHandler 被移除");
        } else {
            System.out.println("无登陆验证，强制关闭连接！");
        }
    }
}
