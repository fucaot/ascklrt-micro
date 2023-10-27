package com.ascklrt.order.engine.event;

import com.fuint.repository.model.zjqh.order.business.MembershipData;
import com.fuint.repository.model.zjqh.order.enums.OrderStatus;
import com.fuint.repository.model.zjqh.order.enums.OrderType;
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
     * 订单类型，参考枚举 OrderType；目前仅用于会员服务
     */
    private OrderType type;

    /**
     * 订单描述
     */
    private String description;

    /**
     * 订单备注
     */
    private String remark;

    private MembershipData membershipData;

    @Override
    public OrderStatus status() {
        return OrderStatus.CREATE;
    }

    @Override
    public String getOrderNum() {
        return null;
    }
}
