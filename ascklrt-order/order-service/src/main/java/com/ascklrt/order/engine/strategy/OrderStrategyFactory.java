package com.ascklrt.order.engine.strategy;

import com.ascklrt.order.enums.OrderStatus;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderStrategyFactory implements InitializingBean, ApplicationContextAware {

    private final Map<OrderStatus, OrderStrategy> STRATEGY_MAP = new HashMap<>();

    private ApplicationContext applicationContext;

    public OrderStrategy laodStrategy(OrderStatus status) {
        OrderStrategy strategy = STRATEGY_MAP.get(status);
        return strategy;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        applicationContext.getBeansOfType(OrderStrategy.class)
                .values()
                .forEach(strategy -> STRATEGY_MAP.put(strategy.status(), strategy));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
