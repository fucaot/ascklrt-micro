package com.ascklrt.order.engine;

import com.ascklrt.order.engine.event.OrderCreateEvent;
import com.ascklrt.order.engine.event.OrderEvent;
import com.ascklrt.order.engine.strategy.OrderStrategy;
import com.ascklrt.order.engine.strategy.OrderStrategyFactory;
import com.ascklrt.order.model.Order;
import com.ascklrt.order.service.IOrderReadService;
import com.google.common.util.concurrent.Striped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;

@Component
public class OrderEngine {

    private final Logger logger = LoggerFactory.getLogger(OrderEngine.class);

    // Guava Striped本地锁
    // 设置32分段，理论上同一时间不会超过32个线程同时操作32个不同订单，超过此数量，则阻塞
    private static final Striped<Lock> LOCKS = Striped.lazyWeakLock(32);

    @Autowired
    OrderStrategyFactory strategyFactory;

    @Autowired
    IOrderReadService orderReadService;

    public <T extends OrderEvent> OrderContext<T> change(T event) {
        Lock lock = LOCKS.get(event);
        lock.lock();

        // 1. 构建上下文
        OrderContext<T> context = getContext(event);
        OrderStrategy<T> strategy = strategyFactory.laodStrategy(event.status());
        try {
            // 参数校验
            strategy.vaildate(context);

            // 策略行为
            strategy.action(context);

            // 持久化
            strategy.save(context);

            // 后置流程
            strategy.after(context);

            return context;
        } catch (Exception e) {
            // if (e instanceof BusinessCheckException) {
            //     logger.error("「订单流转遇到业务异常」: {}", e.getMessage());
            //     throw e;
            // }
            // logger.error("「订单流转遇到服务异常」: ", e);
            throw e;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 构建上下文对象
     * @param event
     * @return
     * @param <T>
     */
    <T extends OrderEvent> OrderContext<T> getContext(T event) {
        OrderContext<T> tOrderContext = new OrderContext<>(event);
        dataPreparation(tOrderContext);
        return tOrderContext;
    }

    /**
     * 数据装载 现有订单
     * @param orderContext
     * @param <T>
     */
    <T extends OrderEvent> void dataPreparation(OrderContext<T> orderContext) {
        if (orderContext.getEvent() instanceof OrderCreateEvent) {
            return;
        }
        Order order = orderReadService.load(orderContext.getEvent().getOrderNum());
        orderContext.setBaseOrder(order);
    }

    Lock lock(OrderEvent event) {
        String key = generateLockkey(event);
        Lock lock = LOCKS.get(key);
        return lock;
    }

    String generateLockkey(OrderEvent event) {
        if (event instanceof OrderCreateEvent) {
            OrderCreateEvent createEvent = (OrderCreateEvent) event;
            // 创建订单不根据订单号，而是根据关键信息来进行匿等
            new StringBuffer()
                    .append("LOCK_ORDER_CREATE:")
                    .append(createEvent.getUserId())
                    .append(":")
                    .append(createEvent.getAmount());
        }
        return "LOCK_ORDER_EVENT:" + event.getOrderNum();
    }
}
