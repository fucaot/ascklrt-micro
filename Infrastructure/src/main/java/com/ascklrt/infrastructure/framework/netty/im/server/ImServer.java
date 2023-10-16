package com.ascklrt.infrastructure.framework.netty.im.server;

import com.ascklrt.infrastructure.framework.netty.im.server.handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class ImServer {

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

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
                                ch.pipeline()
                                        .addLast(new ServerHandler());
                            }
                        })
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
