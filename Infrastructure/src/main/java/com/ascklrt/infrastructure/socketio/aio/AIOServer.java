package com.ascklrt.infrastructure.socketio.aio;

public class AIOServer {


    public static void main(String[] args) {
        AsyncTimeServerHandler asyncTimeServerHandler = new AsyncTimeServerHandler(8080);
        new Thread(asyncTimeServerHandler, "AIO-AysncTimeServerHandler-001").start();
    }
}
