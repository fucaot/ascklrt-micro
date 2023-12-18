package com.ascklrt.infrastructure.juc;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class CountDownLatchApply {

    public static void maina(String[] args) {
        List<Integer> ids = Arrays.asList(1, 2, 3, 4, 5, 6, 7);

        CountDownLatch countDownLatch = new CountDownLatch(ids.size());
        for (Integer id: ids) {
            // 执行寻找工作
            new Thread(new SearchTask(countDownLatch, id)).run();
        }

        try {
            countDownLatch.await();
            System.out.println("7个任务全部执行完成");
        } catch (Exception e) {}
    }

    public static class SearchTask implements Runnable {

        private CountDownLatch latch;

        private Integer id;

        public SearchTask(CountDownLatch latch, Integer id) {
            this.latch = latch;
            this.id = id;
        }

        @Override
        public void run() {
            latch.countDown();
        }
    }
}
