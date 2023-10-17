package com.ascklrt.infrastructure.framework.netty.im.client;

import com.ascklrt.infrastructure.framework.netty.im.client.handler.ClientHandler;
import com.ascklrt.infrastructure.framework.netty.im.client.handler.LoginResponseHandler;
import com.ascklrt.infrastructure.framework.netty.im.client.handler.MessageResponseHandler;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.response.LoginResponsePacket;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.response.MessageResponsePacket;
import com.ascklrt.infrastructure.framework.netty.im.protocol.encode.PacketCodeC;
import com.ascklrt.infrastructure.framework.netty.im.protocol.command.request.MessageRequestPacket;
import com.ascklrt.infrastructure.framework.netty.im.protocol.encode.PacketDecoder;
import com.ascklrt.infrastructure.framework.netty.im.protocol.encode.PacketEncode;
import com.ascklrt.infrastructure.framework.netty.im.util.LoginUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.Scanner;
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
                                .addLast(new PacketDecoder())
                                .addLast(new ClientHandler())
                                .addLast(new LoginResponseHandler())
                                .addLast(new MessageResponseHandler())
                                .addLast(new PacketEncode());
                    }
                });

        connect(bootstrap, "127.0.0.1", 8080, 3);
    }

    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            // 连接成功
            if (future.isSuccess()) {
                System.out.println("连接成功！");

                // 连接成功，启动控制台线程
                Channel channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);
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


    private static void startConsoleThread(Channel channel) {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (LoginUtil.hasLogin(channel)) {
                    System.out.println("输入消息发送至服务端: ");
                    Scanner sc = new Scanner(System.in);
                    String line = sc.nextLine();

                    MessageRequestPacket messageRequestPacket = new MessageRequestPacket();
                    messageRequestPacket.setMessage(line);
                    ByteBuf encode = PacketCodeC.INSTANCE.encode(channel.alloc().buffer(), messageRequestPacket);
                    channel.writeAndFlush(encode);
                }
            }
        }).start();
    }
}
