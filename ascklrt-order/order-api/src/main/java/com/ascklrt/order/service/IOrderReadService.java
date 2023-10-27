package com.ascklrt.order.service;

import com.ascklrt.order.model.Order;

/**
 * @author goumang
 * @description
 * @date 2023/2/13 16:50
 */
public interface IOrderReadService {

    String test();

    Order load(String orderNum);

    // List<Order> query(OrderQueryDTO query, OrderStatus... orderStatuses);
}
