package com.ascklrt.order.engine.strategy;

import com.fuint.common.zjqh.order.engine.OrderContext;
import com.fuint.common.zjqh.order.engine.event.OrderEvent;
import com.fuint.repository.model.zjqh.order.enums.OrderStatus;

public interface OrderStrategy<T extends OrderEvent> {

    OrderStatus status();

    /**
     * 校验
     */
    void vaildate(OrderContext<T> orderContext);

    /**
     * 对订单的行为
     */
    void action(OrderContext<T> orderContext);

    /**
     * 持久化
     */
    void save(OrderContext<T> orderContext);

    /**
     * 后续流程
     */
    void after(OrderContext<T> orderContext);
}