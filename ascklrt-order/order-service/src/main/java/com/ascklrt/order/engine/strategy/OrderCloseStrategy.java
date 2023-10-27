package com.ascklrt.order.engine.strategy;

import com.fuint.common.util.CopyUtil;
import com.fuint.common.zjqh.order.engine.OrderContext;
import com.fuint.common.zjqh.order.engine.event.OrderCloseEvent;
import com.fuint.common.zjqh.order.service.OrderWriteService;
import com.fuint.framework.exception.BusinessCheckException;
import com.fuint.framework.exception.BusinessException;
import com.fuint.repository.model.zjqh.order.Order;
import com.fuint.repository.model.zjqh.order.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class OrderCloseStrategy implements OrderStrategy<OrderCloseEvent>{

    @Autowired
    private OrderWriteService orderWriteService;

    @Override
    public OrderStatus status() {
        return OrderStatus.CLOSE;
    }

    @Override
    public void vaildate(OrderContext<OrderCloseEvent> orderContext) {
        // 已完成订单无法关单
        Order order = orderContext.getBaseOrder();
        if (!Objects.equals(order.getStatus(), OrderStatus.COMPLETE)) {
            throw new BusinessCheckException(BusinessException.ORDER_STATUS_INCORRECT);
        }
    }

    @Override
    public void action(OrderContext<OrderCloseEvent> orderContext) {
        Order baseOrder = orderContext.getBaseOrder();

        Order order = CopyUtil.copy(baseOrder, Order.class);
        order.setStatus(OrderStatus.CLOSE);
        orderContext.setOrder(order);
    }

    @Override
    public void save(OrderContext<OrderCloseEvent> orderContext) {
        orderWriteService.modify(orderContext.getOrder());
    }

    @Override
    public void after(OrderContext<OrderCloseEvent> orderContext) {}
}
