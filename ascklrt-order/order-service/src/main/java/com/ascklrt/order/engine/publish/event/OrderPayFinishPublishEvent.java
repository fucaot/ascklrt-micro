package com.ascklrt.order.engine.publish.event;

import com.fuint.repository.model.zjqh.order.Order;

public class OrderPayFinishPublishEvent extends AbstractOrderEvent<Order>{
    public OrderPayFinishPublishEvent(Object source, Order order) {
        super(source, order);
    }
}
