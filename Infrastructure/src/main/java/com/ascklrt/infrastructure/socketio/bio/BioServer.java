package com.ascklrt.infrastructure.socketio.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class BioServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(8080);
            Socket socket = null;

            // 创建I/O线程任务池
            TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool(50, 1000);

            while (true) {
                socket = serverSocket.accept();
                singleExecutor.execute(new TimeServerHandler(socket));
            }
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
                serverSocket = null;
            }
        }
    }


    public static class TimeServerHandlerExecutePool {
        private ExecutorService executor;

        public TimeServerHandlerExecutePool(int maxPoolSize, int queueSize) {
            executor = new ThreadPoolExecutor(
                    Runtime.getRuntime().availableProcessors(),     // 核心线程数
                    maxPoolSize,                                    // 最大线程数量
                    120L,                                           // 等待时间
                    TimeUnit.SECONDS,                               // 时间单位
                    new ArrayBlockingQueue<Runnable>(queueSize)     // 队列

            );
        }

        public void execute(Runnable task) {
            executor.execute(task);
        }
    }


    public static class TimeServerHandler implements Runnable {

        private Socket socket;

        public TimeServerHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
        }
    }
}
