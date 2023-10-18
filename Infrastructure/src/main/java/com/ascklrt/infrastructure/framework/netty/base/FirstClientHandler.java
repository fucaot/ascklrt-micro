package com.ascklrt.infrastructure.framework.netty.base;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(new Date() + ": 客户端写入数据");

        // // 测试粘包
        // for (int i = 0; i < 1000; i++) {
        //     ByteBuf byteBufNian = getByteBufNian(ctx);
        //     // 2. 写数据
        //     ctx.channel().writeAndFlush(byteBufNian);
        // }

        // 1. 获取数据
        ByteBuf buffer = getByteBuf(ctx);

        // 2. 写数据
        ctx.channel().writeAndFlush(buffer);
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        // 1. 获取二进制抽象ByteBuf
        ByteBuf buffer = ctx.alloc().buffer();



        // 2. 准备数据，指定数据的字符集为UTF-8
        byte[] bytes = "hello netty".getBytes(StandardCharsets.UTF_8);
        buffer.writeBytes(bytes);
        return buffer;
    }
    private static ByteBuf getByteBufNian(ChannelHandlerContext ctx) {
        byte[] bytes = "你好，这是一条关于粘包的网络测试消息！！".getBytes(Charset.forName("utf-8"));
        ByteBuf buffer = ctx.alloc().buffer();
        ByteBuf byteBuf = buffer.writeBytes(bytes);
        return byteBuf;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buffer = (ByteBuf) msg;
        System.out.println(new Date() + ": 客户端读到数据： " + buffer.toString(StandardCharsets.UTF_8));
    }
}
