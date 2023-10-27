package com.ascklrt.order.engine.publish.event;

import org.springframework.context.ApplicationEvent;

public abstract class AbstractOrderEvent<Order> extends ApplicationEvent {

    private Order order;

    public AbstractOrderEvent(Object source, Order order) {
        super(source);
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
