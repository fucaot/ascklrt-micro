package com.ascklrt.order.engine;

import com.ascklrt.order.engine.event.OrderEvent;
import com.ascklrt.order.model.Order;
import lombok.Data;

@Data
public class OrderContext<T extends OrderEvent> {

    private T event;

    private Order order;

    private Order baseOrder;

    public OrderContext(T event) {
        this.event = event;
    }
}
