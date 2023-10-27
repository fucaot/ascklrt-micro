package com.ascklrt.order.engine.event;

import com.fuint.repository.model.zjqh.order.enums.OrderStatus;

public class OrderPayFinishEvent implements OrderEvent {

    private String orderNum;

    @Override
    public OrderStatus status() {
        return OrderStatus.PAY_FINISH;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String getOrderNum() {
        return orderNum;
    }
}
