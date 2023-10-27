package com.ascklrt.order.engine.event;


import com.ascklrt.order.enums.OrderStatus;

public interface OrderEvent {

    abstract OrderStatus status();

    String getOrderNum();

}