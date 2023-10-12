package com.ascklrt.infrastructure.framework.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Date;

public class NettyClient {

    public static final int MAX_RETRY = 3;

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group)                                                      // 指定线程组模型
                .channel(NioSocketChannel.class)                                    // 指定IO模型为NIO
                .handler(new ChannelInitializer<SocketChannel>() {                  // 处理逻辑
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new StringEncoder());
                    }
                });


        // Channel channel = bootstrap.connect("127.0.0.1", 8080).channel();

        while (true) {
            channel.writeAndFlush(new Date() + "hello world");
            Thread.sleep(2000);
        }
    }


    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            // 连接成功
            if (future.isSuccess()) {
                System.out.println("连接成功！");
            } else {
                System.out.println("链接失败！");
                int order = (MAX_RETRY - retry) + 1;

                // 计算本次连接的间
                int delay = 1 << order;


            }
        });
    }
}
