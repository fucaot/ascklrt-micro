package com.ascklrt.web.order.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author goumang
 * @description
 * @date 2023/2/14 10:54
 */
@SpringBootTest
public class OrderControllerTest {

    @Autowired
    private OrderController orderController;

    @Test
    public void getOrderTest() {
        orderController.getOrder();
    }

}
