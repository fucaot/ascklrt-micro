package com.ascklrt.infrastructure.juc.lock;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author goumang
 * @description
 * @date 2023/3/8 13:54
 */
public class Sync {

    private static long n = 0;

    public static void maina(String[] args) throws InterruptedException {
        int i = 0;

        Thread[] threads = new Thread[100];
        CountDownLatch latch = new CountDownLatch(threads.length);

        for (i = 0; i < threads.length; i ++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    /**
                     * 把n读取到寄存器 + 1，写入回去0
                     * 并发导致的问题就是n是98的时候，两个线程都获取到98，并+1写回，本来应该是100，结果写入回去是99
                     *
                     * synchronized 让不同线程之间的操作变成序列化的，必须上一个线程操作完下一个线程才能获取资源
                     */
                    synchronized (Sync.class) {
                        // 临界区
                        n = n + 1 ;
                    }
                }
                latch.countDown();
            });
        }

        for (Thread thread : threads) {
            thread.start();
        }

        latch.await();

        System.out.println(n);
    }


    static Integer num = 0;

    @Test
    public void another() throws InterruptedException {

        for (int i = 0; i < 3; i++) {
            Thread thread = new Thread(() -> {
                synchronized (num) {
                    while (num < 1000) {
                        num = num + 1;
                        System.out.println(Thread.currentThread().getName() + ": " + num);
                    }
                }
            });
            thread.start();
        }
        Thread.sleep(1000);
    }
}
