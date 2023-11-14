package com.ascklrt.infrastructure.juc.lock;

import java.util.concurrent.atomic.AtomicInteger;

public class Cas {

    /**
     * 无锁编程，底层使用cas实现了计数器
     */
    static AtomicInteger num = new AtomicInteger(0);

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                while (num.get() < 1000) {
                    System.out.println(Thread.currentThread().getName() + ": " + num.incrementAndGet());
                }
            }).start();
        }
    }
}
