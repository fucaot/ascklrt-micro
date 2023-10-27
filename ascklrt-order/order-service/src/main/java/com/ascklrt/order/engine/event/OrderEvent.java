package com.ascklrt.order.engine.event;

import com.fuint.repository.model.zjqh.order.enums.OrderStatus;

public interface OrderEvent {

    abstract OrderStatus status();

    String getOrderNum();

}