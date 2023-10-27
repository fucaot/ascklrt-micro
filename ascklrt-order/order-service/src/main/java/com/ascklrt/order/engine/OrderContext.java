package com.ascklrt.order.engine;

import com.fuint.common.zjqh.order.engine.event.OrderEvent;
import com.fuint.repository.model.zjqh.order.Order;
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
