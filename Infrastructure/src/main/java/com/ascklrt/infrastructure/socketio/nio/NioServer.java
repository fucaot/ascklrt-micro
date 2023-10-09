package com.ascklrt.infrastructure.socketio.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) throws IOException {
        /*
         * 1. 打开一个ServerSocketChannel，用于建厅客户端的链接，是所有客户端连接的父管道
         * Channel是一个通道，可以通过它读取和写入数据，它就像自来水管一样，网络数据通过Channel读取和
         * 写入。通道与流的不同之处在于通道是双向的，流只是在一个方向上移动（一个流必须是InputStream或
         * 者OutputStream的子类），并且通道可以用于读、写或者同时用于读写。
         * 因为Channel是全双工的，所以它可以比流更好的映射底层操作系统的API。特别是在UNIX网络编程模型
         * 中，底层操作系统的通道都是全双工的，同时支持读写操作。
         *
         * 实际上Channel可以分两大类：分别适用于网络读写的SelectableChannel和用于文件操作的
         * FileChannel。
         * ServerSocketChannel和SocketChannel都是SelectableChannel的子类。
         */
        ServerSocketChannel acceptorSvr = ServerSocketChannel.open();

        // 2.1 绑定监听端口，设置链接为非阻塞
        acceptorSvr.socket()
                .bind(
                        new InetSocketAddress(8080)
                );
        // 2.2 设置非阻塞
        acceptorSvr.configureBlocking(false);

        /*
         * 3. 创建Reactor线程，创建多路复用器并启动代码
         * 个多路复用器Selector可以同时轮询多个Channel，由于JDK使用了epoll()代替传统的select实现，
         * 所以它并没有最大连接句柄1024/2048的限制。这也就意味着只需要一个线程负责Selector的轮询，就
         * 可以接入成千上万的客户端，这确实是个非常巨大的进步。                   -- Netty权威指南
         */
        Selector selector = Selector.open();
        new Thread(new ReactorTask()).start();

        /**
         * 4. 将ServerSocketChannel注册到Reactor线程的多路复用器Selector上，监听Accept（链接）事件
         * - OP_ACCEPT（接受连接操作）：表示一个服务器通道已经准备好接受一个新的连接。当一个客户端尝试连
         * 接到服务器时，服务器通道将注册一个 OP_ACCEPT 事件，以便在有新连接到达时进行处理。
         * - OP_CONNECT（连接就绪操作）：表示一个客户端通道已经准备好连接到远程服务器。这通常与非阻塞套接
         * 字通道一起使用，以确定连接是否已建立。
         * - OP_READ（读操作）：表示一个通道已经准备好进行读取操作。当从通道读取数据时，通常会注册一个
         * OP_READ 事件。
         * - OP_WRITE（写操作）：表示一个通道已经准备好进行写入操作。当向通道写入数据时，通常会注册一个
         * OP_WRITE 事件。
         */
        acceptorSvr.register(selector, SelectionKey.OP_ACCEPT);



        // 5. 多路复用器在线程run方法的无限循环体内轮训准备就绪的key
        while(true) {
            /*
             * 5.1 阻塞等待事件发生
             * 在选择器（Selector）上进行阻塞式的事件监听，等待感兴趣的事件发生，并返回事件的数量
             *
             * 具体来说，这个方法会阻塞程序的执行，直到以下事件之一发生：
             *
             * - 有一个或多个通道已准备好进行 I/O 操作，如接受连接、读取或写入数据。
             * - 选择器的 wakeup 方法被调用，手动唤醒阻塞的 select 操作。
             * - 调用线程被中断（即线程的 interrupt 方法被调用）。
             */
            int num = selector.select();

            // 5.2 获取事件发生的SelectionKey集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();

                // 6. 处理客户端链接请求
                if (key.isAcceptable()) {
                    // 6.1 获取channel，完成tcp三次握手
                    // 获取服务端channel
                    ServerSocketChannel svrChannel = (ServerSocketChannel)key.channel();

                    // 根据服务端channel接收到新建立的clientChannel
                    // 此处的channel不是上面创建的serverChannel，而是每一个链接进来都会生成一个新的
                    // channel，即使同时有多个不同的 ServerSocketChannel 上有网络请求，每个
                    // SelectionKey 仍然会与其对应的 SocketChannel 关联
                    SocketChannel channel = svrChannel.accept();

                    // 也可以这么写，但是建议以上写法：
                    // SocketChannel accept = acceptorSvr.accept();

                    // 7. 设置非阻塞
                    channel.configureBlocking(false);
                    channel.socket().setReuseAddress(true);

                    // 8. 将本次链接生成的新的channel注册到reactor，待这个channel事件变为可读则
                    // goto：70行代码，最终走到下面处98行处理可读事件逻辑。
                    channel.register(selector, SelectionKey.OP_READ);
                }

                // 9. 处理可读事件
                if (key.isReadable()) {
                    // 9.1 获取可读channel
                    SocketChannel channel = (SocketChannel) key.channel();

                    /**
                     * 9.2 创建buffer读取数据
                     * Buffer是一个对象，它包含一些要写入或者要读出的数据。在NIO类库中加入Buffer对象，体现了
                     * 新库与原I/O的一个重要区别。在面向流的I/O中，可以将数据直接写入或者将数据直接读到Stream
                     * 对象中。
                     * 在NIO库中，所有数据都是用缓冲区处理的。在读取数据时，它是直接读到缓冲区中的；在写入数据时
                     * ，写入到缓冲区中。任何时候访问NIO中的数据，都是通过缓冲区进行操作。
                     * 缓冲区实质上是一个数组。通常它是一个字节数组(ByteBuffer)，也可以使用其他种类的数组。但是
                     * 一个缓冲区不仅仅是一个数组，缓冲区提供了对数据的结构化访问以及维护读写位置(limit)等信息。
                     * 最常用的缓冲区是ByteBuffer，一个ByteBuffer提供了一组功能用于操作byte数组。除了ByteBuffer
                     * ，还有其他的一些缓冲区，事实上，每一种Java基本类型（除了Boolean类型）都对应
                     * 有一种缓冲区。
                     *
                     * 如果我要做一个框架来处理http请求，那么在这里接收到tcp byte数组消息流后就可以开始对http格式
                     * 进行解析了。
                     */
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int read = channel.read(buffer);

                    // 9.3 对ByteBuffer进行编解码，如果有半包消息指针rest，继续读取后续的报文，将解码成功的消息
                    // 封装成Task，投递到业务线程池中，进行业务编排逻辑。
                    Object message = null;

                    // 需要先将buffer的模式转换为读模式
                    buffer.flip();

                    // 方法一、 逐个字节读取
                    // StringBuffer data = new StringBuffer();
                    // while(buffer.hasRemaining()) {
                    //     buffer.mark();

                    //     // 此处最好进行utf-8解编码处理
                    //     char c = (char) buffer.get();

                    //     data.append(c);
                    // }

                    // 方法2、将数据读取到字节数组，记得进行编码处理

                    byte[] data = new byte[buffer.remaining()];
                    buffer.get(data);
                    String body = new String(data, "UTF-8");

                    System.out.println(body);
                    // ...业务处理

                    // 回复信息
                    ByteBuffer responseBuffer = ByteBuffer.wrap(data.toString().getBytes());
                    channel.write(responseBuffer);
                }

                // 处理完毕，删除事件
                iterator.remove();
            }
        }

    }

    public static class ReactorTask implements Runnable {

        @Override
        public void run() {

        }
    }
}