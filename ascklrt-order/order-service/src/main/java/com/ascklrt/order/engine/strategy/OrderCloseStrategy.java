package com.ascklrt.order.engine.strategy;

import com.ascklrt.common.util.CopyUtil;
import com.ascklrt.order.engine.OrderContext;
import com.ascklrt.order.engine.event.OrderCloseEvent;
import com.ascklrt.order.enums.OrderStatus;
import com.ascklrt.order.model.Order;
import com.ascklrt.order.service.IOrderWriteService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class OrderCloseStrategy implements OrderStrategy<OrderCloseEvent>{

    @Autowired
    private IOrderWriteService orderWriteService;

    @Override
    public OrderStatus status() {
        return OrderStatus.CLOSE;
    }

    @Override
    public void vaildate(OrderContext<OrderCloseEvent> orderContext) {
        // 已完成订单无法关单
        Order order = orderContext.getBaseOrder();
        if (!Objects.equals(order.getStatus(), OrderStatus.COMPLETE)) {
            // throw new BusinessCheckException(BusinessException.ORDER_STATUS_INCORRECT);
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
