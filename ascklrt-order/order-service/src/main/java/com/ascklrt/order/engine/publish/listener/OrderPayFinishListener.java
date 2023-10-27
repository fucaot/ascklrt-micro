package com.ascklrt.order.engine.publish.listener;

import com.fuint.common.service.MemberService;
import com.fuint.common.zjqh.order.engine.publish.event.OrderPayFinishPublishEvent;
import com.fuint.repository.model.zjqh.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OrderPayFinishListener implements ApplicationListener<OrderPayFinishPublishEvent> {

    private final Logger logger = LoggerFactory.getLogger(OrderPayFinishPublishEvent.class);

    @Autowired
    private MemberService memberService;

    @Override
    public void onApplicationEvent(OrderPayFinishPublishEvent event) {
        Order order = event.getOrder();

        // 1. 会员权益添加
        memberService.upPaidMember(order.getUserId().intValue());
        logger.info("「ORDER」支付成功，会员权益添加成功");
    }
}
