package com.ascklrt.order.engine.strategy;

import cn.hutool.json.JSONUtil;
import com.fuint.common.util.GenerateNum;
import com.fuint.common.zjqh.order.engine.OrderContext;
import com.fuint.common.zjqh.order.engine.event.OrderCreateEvent;
import com.fuint.common.zjqh.order.service.OrderWriteService;
import com.fuint.repository.model.zjqh.order.Order;
import com.fuint.repository.model.zjqh.order.business.MembershipData;
import com.fuint.repository.model.zjqh.order.enums.MemberPaidPlan;
import com.fuint.repository.model.zjqh.order.enums.OrderStatus;
import com.fuint.repository.model.zjqh.order.enums.OrderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderCreateStrategy implements OrderStrategy<OrderCreateEvent>{

    @Autowired
    private OrderWriteService orderWriteService;

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

        // 订单状态，目前只有会员服务
        order.setType(event.getType());
        // order.setBusinessData();

        // 3. 备注，用户信息
        order.setUserId(event.getUserId());
        order.setDescription(event.getDescription());
        order.setRemark(event.getRemark());

        // 4. 设置业务数据
        if (order.getType().equals(OrderType.MEMBERSHIP_SERVICE)) {
            MembershipData membershipData = event.getMembershipData();
            MemberPaidPlan memberPaidPlan = membershipData.getMemberPaidPlan();
            order.setBusinessData(JSONUtil.toJsonStr(membershipData));
            order.setAmount(memberPaidPlan.getPrice());
            order.setPayableAmount(memberPaidPlan.getPrice());
            order.setPaidAmount(memberPaidPlan.getPrice());
        }

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