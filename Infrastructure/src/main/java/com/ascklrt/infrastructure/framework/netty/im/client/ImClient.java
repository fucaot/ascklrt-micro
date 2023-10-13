package com.ascklrt.infrastructure.framework.netty.im.client;

import com.ascklrt.infrastructure.framework.netty.im.client.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ImClient {

    public static final int MAX_RETRY = 3;

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();

        bootstrap
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new ClientHandler());
                    }
                });

        connect(bootstrap, "127.0.0.1", 8080, 3);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            // 连接成功
            if (future.isSuccess()) {
                System.out.println("连接成功！");
            } else if (retry == 0) {
                System.out.println("次数已用完，放弃链接！");
            } else {
                System.out.println("链接失败！");
                int order = (MAX_RETRY - retry) + 1;

                // 计算本次连接的间
                int delay = 1 << order;
                int newRetry = retry - 1;

                System.out.println(new Date() + "连接失败，第" + order + "次重试..");

                // 延时执行链接
                bootstrap
                        .config()
                        .group()
                        .schedule(
                                () -> connect(bootstrap, host, port, newRetry), delay, TimeUnit.SECONDS
                        );
            }
        });
    }
}
