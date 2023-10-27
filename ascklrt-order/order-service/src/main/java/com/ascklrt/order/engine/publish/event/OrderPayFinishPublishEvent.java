package com.ascklrt.order.engine.publish.event;

import com.ascklrt.order.model.Order;

public class OrderPayFinishPublishEvent extends AbstractOrderEvent<Order>{
    public OrderPayFinishPublishEvent(Object source, Order order) {
        super(source, order);
    }
}
