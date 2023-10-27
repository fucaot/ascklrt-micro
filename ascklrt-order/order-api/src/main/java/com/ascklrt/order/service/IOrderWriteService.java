package com.ascklrt.order.service;

import com.ascklrt.order.model.Order;

public interface IOrderWriteService {

    void saveOrder(Order order);

    void modify(Order order);
}