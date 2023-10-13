package com.ascklrt.infrastructure.framework.netty.base;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class NettyServer {

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        /**
         * 首先创建两个线程组
         * bootGroup表示监听端口，接收新连接的线程组。
         * boss对应IOServer.java中的负责接收新连接的线程，主要负责创建新连接。
         *
         * workerGroup表示处理每一个连接读写的线程组。
         * worker对应IOServer.java中的负责读取数据的线程，主要用于读取数据及业务逻辑处理。
         */
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        // serverbootstrap作为引导类，引导服务启动工作
        serverBootstrap
                .group(boss, worker)                                        // 为引导类配置线程组
                .channel(NioServerSocketChannel.class)                      // 指定IO模型为NIO（尚且为止此处的NIO是否为异步非堵塞IO，也就是未知对应NIO还是NIO2.0）
                .handler(                                                   // handler()方法用于指定在服务端启动过程中的一些逻辑，此处的通道是server接收通道
                        new ChannelInitializer<NioServerSocketChannel>() {
                            @Override
                            protected void initChannel(NioServerSocketChannel nioSocketChannel) throws Exception {
                                System.out.println("NeetyServer - 启动成功！");
                            }
                        })
                .childHandler(                                              // childHandler()方法用于指定处理新连接数据的读写处理逻辑，此处的通道是每次链接的通道
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new FirstServerHandler());
                                // ch.pipeline().addLast(new StringDecoder());
                                // ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                                //     @Override
                                //     protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
                                //         System.out.println(msg);
                                //     }
                                // });
                            }
                        })
                // .option(ChannelOption.SO_BACKLOG, 1024)                  // option()方法可以给服务端Channel设置一些TCP参数，最常见的就是so_backlog
                // .childOption(ChannelOption.SO_KEEPALIVE, true)           // childOption()方法可以给每个连接都设置一些TCP参数
        ;
        // 绑定端口
        bind(serverBootstrap, 8080);
    }

    /**
     * 递增绑定，直到找到可以绑定的端口
     *
     * @param serverBootstrap
     * @param port
     */
    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        if (port > 8090) {
            return;
        }
        serverBootstrap.bind(port)
                // 绑定端口返回一个Future对象，添加对这个对象的监听，若绑定失败则递增寻找端口
                .addListener(new GenericFutureListener<Future<? super Void>>() {
                    @Override
                    public void operationComplete(Future<? super Void> future) throws Exception {
                        if (future.isSuccess()) {
                            System.out.println("端口 " + port + " 绑定成功！");
                        } else {
                            System.out.println("端口 " + port + " 绑定失败！");
                            bind(serverBootstrap, port + 1);
                        }
                    }
                });
    }
}
