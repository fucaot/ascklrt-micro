package com.ascklrt.order.engine.event;

import com.ascklrt.order.enums.OrderStatus;
import lombok.Data;

@Data
public class OrderCreateEvent implements OrderEvent{

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单金额
     */
    private Long amount;


    /**
     * 订单描述
     */
    private String description;

    /**
     * 订单备注
     */
    private String remark;

    @Override
    public OrderStatus status() {
        return OrderStatus.CREATE;
    }

    @Override
    public String getOrderNum() {
        return null;
    }
}
