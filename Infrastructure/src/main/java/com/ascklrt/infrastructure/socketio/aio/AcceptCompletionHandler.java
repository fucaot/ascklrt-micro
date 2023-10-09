package com.ascklrt.infrastructure.socketio.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;


public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, AsyncTimeServerHandler> {
    @Override
    public void completed(AsynchronousSocketChannel result, AsyncTimeServerHandler attachment) {
        /**
         * 既然已经接收客户端成功了，为什么还要再次调用accept方法呢？
         * 原因是这样的：当我们调用AsynchronousServerSocketChannel的accept方法后，如果有新的客户端连接接
         * 入，系统将回调我们传入的CompletionHandler实例的completed方法，表示新的客户端已经接入成功，因为一
         * 个AsynchronousServerSocket Channel可以接收成千上万个客户端，所以我们需要继续调用它的accept方法，
         * 接收其他的客户端连接，最终形成一个循环。每当接收一个客户读连接成功之后，再异步接收新的客户端连接。
         *
         * 出自《Netty权威指南》 - 2.4章节
         *
         * 那么简单的来说便是，虽然代码运行到这里代表我接收到了一个客户端的链接，但是我要继续accept接收，便可以在非
         * 堵塞的情况下接收到新的客户端链接，在另一个线程中继续走一段代码，因此便可以实现同时接入很多的客户端链接。
         */
        attachment.asynchronousServerSocketChannel.accept(attachment, this);

        // 读取数据，分配1M缓冲区，并进行异步读操作
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        result.read(
                buffer,                             // ByteBuffer dst：接收缓冲区，用于从异步Channel中读取数据包；
                buffer,                             // A attachment：异步Channel携带的附件，通知回调的时候作为入参使用；
                new ReadCompletionHandler(result)   // CompletionHandler<Integer，？super A>：接收通知回调的业务handler，本例程中为ReadCompletionHandler。
        );
    }

    @Override
    public void failed(Throwable exc, AsyncTimeServerHandler attachment) {
        exc.printStackTrace();
        attachment.latch.countDown();
    }
}