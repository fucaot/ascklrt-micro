package com.ascklrt.infrastructure.socketio.aio.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeClientHandler implements CompletionHandler<Void, AsyncTimeClientHandler>, Runnable {

    private AsynchronousSocketChannel client;
    private String host;
    private int port;
    private CountDownLatch latch;

    public AsyncTimeClientHandler(String host, int port) {
        this.host = host;
        this.port = port;

        try {
            client = AsynchronousSocketChannel.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        latch = new CountDownLatch(1);

        client.connect(new InetSocketAddress(host, port), this, this);

        try {
            latch.await();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 由上面client.connect链接成功后的操作，链接成功后开始进行异步写
     * @param result
     *          The result of the I/O operation.
     * @param attachment
     *          The object attached to the I/O operation when it was initiated.
     */
    @Override
    public void completed(Void result, AsyncTimeClientHandler attachment) {
        byte[] req = "QUERY TIME ORDER".getBytes();

        ByteBuffer write = ByteBuffer.allocate(1024);
        write.put(req);
        write.flip();
        client.write(write, write, new CompletionHandler<Integer, ByteBuffer>() {
            /**
             * 异步写完成之后的操作，若还存在剩余字节则继续写，若不存在剩余字节（已经异步写完毕）则进行读取（返回）
             * @param result
             *          The result of the I/O operation.
             * @param buffer
             *          The object attached to the I/O operation when it was initiated.
             */
            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                if (buffer.hasRemaining()) {
                    client.write(buffer, buffer, this);
                } else {
                    // 无剩余字节，开始进行读操作
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    client.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer buffer) {
                            buffer.flip();
                            byte[] bytes = new byte[buffer.remaining()];
                            buffer.get(bytes);

                            String body;
                            try {
                                body = new String(bytes, "UTF-8");
                                System.out.println("Now is : " + body);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {
                            try {
                                client.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    client.close();
                    latch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void failed(Throwable exc, AsyncTimeClientHandler attachment) {
        try {
            client.close();
            latch.countDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
