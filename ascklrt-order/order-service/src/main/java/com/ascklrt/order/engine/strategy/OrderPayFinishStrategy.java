package com.ascklrt.order.engine.strategy;

import com.ascklrt.common.util.CopyUtil;
import com.ascklrt.order.engine.OrderContext;
import com.ascklrt.order.engine.event.OrderPayFinishEvent;
import com.ascklrt.order.engine.publish.event.OrderPayFinishPublishEvent;
import com.ascklrt.order.enums.OrderStatus;
import com.ascklrt.order.model.Order;
import com.ascklrt.order.service.IOrderWriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OrderPayFinishStrategy implements OrderStrategy<OrderPayFinishEvent>{

    private final Logger logger = LoggerFactory.getLogger(OrderPayFinishStrategy.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private IOrderWriteService orderWriteService;

    @Override
    public OrderStatus status() {
        return OrderStatus.PAY_FINISH;
    }

    @Override
    public void vaildate(OrderContext<OrderPayFinishEvent> orderContext) {
        // 1. 校验订单状态是否为待付款
        Order order = orderContext.getBaseOrder();
        if (!Objects.equals(order.getStatus(), OrderStatus.PAYING)) {
            // throw new BusinessCheckException(BusinessException.ORDER_STATUS_INCORRECT);
        }
    }

    @Override
    public void action(OrderContext<OrderPayFinishEvent> orderContext) {
        Order baseOrder = orderContext.getBaseOrder();

        Order order = CopyUtil.copy(baseOrder, Order.class);
        order.setStatus(OrderStatus.PAY_FINISH);
        order.setPaidAmount(order.getPayableAmount());
        orderContext.setOrder(order);
    }

    @Override
    public void save(OrderContext<OrderPayFinishEvent> orderContext) {
        orderWriteService.modify(orderContext.getOrder());
    }

    @Override
    public void after(OrderContext<OrderPayFinishEvent> orderContext) {
        // todo 发送通知
        //发送事件
        logger.info("订单支付完成处理事件发送，orderNum:{}", orderContext.getOrder().getOrderNum());
        OrderPayFinishPublishEvent publishEvent = new OrderPayFinishPublishEvent(this, orderContext.getOrder());
        // ApplicationContext applicationContext = SpringUtil.getApplicationContext();
        //spring事件支持发布自定义事件
        applicationContext.publishEvent(publishEvent);
    }
}
