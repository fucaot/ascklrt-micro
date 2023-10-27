package com.ascklrt.order.engine.event;


import com.ascklrt.order.enums.OrderStatus;

public class OrderCancelEvent implements OrderEvent{

    private String orderNum;

    public OrderCancelEvent(String orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public OrderStatus status() {
        return OrderStatus.CANCEL;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String getOrderNum() {
        return orderNum;
    }
}
