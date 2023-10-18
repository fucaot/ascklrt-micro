package com.ascklrt.infrastructure.framework.netty.base;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;
import java.util.concurrent.TimeUnit;

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
                        // ch.pipeline()返回的是和这条连接相关的逻辑处理链，采用了责任链模式。
                        channel.pipeline()
                                .addLast(new FirstClientHandler());
                    }
                });
                // .option() 方法可以为连接设置一些TCP底层相关的属性
                // .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)  // ChannelOption.CONNECT_TIMEOUT_MILLIS 表示连接的超时时间，超过这个时间，如果仍未连接到服务端，则表示连接失败。
                // .option(ChannelOption.SO_KEEPALIVE, true)            // ChannelOption.SO_KEEPALIVE 表示是否开启TCP底层心跳机制，true表示开启。
                // .option(ChannelOption.TCP_NODELAY, true)             // ChannelOption.TCP_NODELAY 表示是否开始Nagle算法，true表示关闭，false表示开启。通俗地说，如果要求高实时性，
                                                                        // 有数据发送时就马上发送，就设置为true；如果需要减少发送次数，减少网络交互，就设置
                                                                        // 为false。


        // Channel channel = bootstrap.connect("127.0.0.1", 8080).channel();
        connect(bootstrap, "127.0.0.1", 8080, MAX_RETRY);


        // while (true) {
        //     channel.writeAndFlush(new Date() + "hello world");
        //     Thread.sleep(2000);
        // }
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
