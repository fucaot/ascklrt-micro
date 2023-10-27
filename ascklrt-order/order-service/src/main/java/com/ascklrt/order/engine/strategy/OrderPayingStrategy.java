package com.ascklrt.order.engine.strategy;

import com.fuint.common.util.CopyUtil;
import com.fuint.common.zjqh.order.engine.OrderContext;
import com.fuint.common.zjqh.order.engine.event.OrderPayingEvent;
import com.fuint.common.zjqh.order.service.OrderWriteService;
import com.fuint.framework.exception.BusinessCheckException;
import com.fuint.framework.exception.BusinessException;
import com.fuint.repository.model.zjqh.order.Order;
import com.fuint.repository.model.zjqh.order.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OrderPayingStrategy implements OrderStrategy<OrderPayingEvent>{

    @Autowired
    private OrderWriteService orderWriteService;

    @Override
    public OrderStatus status() {
        return OrderStatus.PAYING;
    }

    @Override
    public void vaildate(OrderContext<OrderPayingEvent> orderContext) {
        // 1. 校验订单状态是否为待付款
        Order order = orderContext.getBaseOrder();
        if (!Objects.equals(order.getStatus(), OrderStatus.CREATE)) {
            throw new BusinessCheckException(BusinessException.ORDER_STATUS_INCORRECT);
        }
    }

    @Override
    public void action(OrderContext<OrderPayingEvent> orderContext) {
        Order baseOrder = orderContext.getBaseOrder();

        Order order = CopyUtil.copy(baseOrder, Order.class);
        order.setStatus(OrderStatus.PAYING);
        orderContext.setOrder(order);
    }

    @Override
    public void save(OrderContext<OrderPayingEvent> orderContext) {
        orderWriteService.modify(orderContext.getOrder());
    }

    @Override
    public void after(OrderContext<OrderPayingEvent> orderContext) {
        // todo 发送通知
    }
}
