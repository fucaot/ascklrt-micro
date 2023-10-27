package com.ascklrt.order.engine.strategy;

import cn.hutool.json.JSONUtil;
import com.ascklrt.common.util.GenerateNum;
import com.ascklrt.order.engine.OrderContext;
import com.ascklrt.order.engine.event.OrderCreateEvent;
import com.ascklrt.order.enums.OrderStatus;
import com.ascklrt.order.model.Order;
import com.ascklrt.order.service.IOrderWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderCreateStrategy implements OrderStrategy<OrderCreateEvent>{

    @Autowired
    private IOrderWriteService orderWriteService;

    @Override
    public OrderStatus status() {
        return OrderStatus.CREATE;
    }

    @Override
    public void vaildate(OrderContext<OrderCreateEvent> orderContext) {

    }

    @Override
    public void action(OrderContext<OrderCreateEvent> orderContext) {
        OrderCreateEvent event = orderContext.getEvent();
        Order order = new Order();

        // 创建订单
        order.setStatus(OrderStatus.CREATE);

        // 1. 生成订单编号
        String num = GenerateNum.getNum();
        order.setOrderNum(num);

        // 2. 计算金额相关
        order.setAmount(event.getAmount());
        order.setPayableAmount(event.getAmount());
        order.setPaidAmount(event.getAmount());

        // 3. 备注，用户信息
        order.setUserId(event.getUserId());
        order.setDescription(event.getDescription());
        order.setRemark(event.getRemark());

        orderContext.setOrder(order);
    }

    @Override
    public void save(OrderContext<OrderCreateEvent> orderContext) {
        orderWriteService.saveOrder(orderContext.getOrder());
    }

    @Override
    public void after(OrderContext<OrderCreateEvent> orderContext) {
        // 发送订单通知
    }
}