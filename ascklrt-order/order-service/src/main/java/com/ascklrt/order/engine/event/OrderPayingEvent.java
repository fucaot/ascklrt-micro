package com.ascklrt.order.engine.event;

import com.ascklrt.order.enums.OrderStatus;

public class OrderPayingEvent implements OrderEvent{

    private String orderNum;

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public OrderStatus status() {
        return OrderStatus.PAYING;
    }

    @Override
    public String getOrderNum() {
        return orderNum;
    }
}
