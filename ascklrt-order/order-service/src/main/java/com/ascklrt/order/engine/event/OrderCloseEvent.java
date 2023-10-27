package com.ascklrt.order.engine.event;

import com.ascklrt.order.enums.OrderStatus;

public class OrderCloseEvent implements OrderEvent{

    private String orderNum;

    @Override
    public OrderStatus status() {
        return OrderStatus.CLOSE;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String getOrderNum() {
        return orderNum;
    }
}
