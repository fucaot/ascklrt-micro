package com.ascklrt.order.engine.publish.listener;

import com.ascklrt.order.engine.publish.event.OrderPayFinishPublishEvent;
import com.ascklrt.order.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OrderPayFinishListener implements ApplicationListener<OrderPayFinishPublishEvent> {

    private final Logger logger = LoggerFactory.getLogger(OrderPayFinishPublishEvent.class);


    @Override
    public void onApplicationEvent(OrderPayFinishPublishEvent event) {
        Order order = event.getOrder();
    }
}
