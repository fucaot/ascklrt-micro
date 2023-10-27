package com.ascklrt.order.model;

import com.ascklrt.order.enums.OrderStatus;
import lombok.Data;

import java.util.Date;

@Data
public class Order {

    /**
     * 订单编号
     */
    private String orderNum;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 订单状态
     */
    private OrderStatus status;

    /**
     * 订单金额
     */
    private Long amount;

    /**
     * 应付款金额（以分为单位）
     */
    private Long payableAmount;

    /**
     * 已支付金额
     */
    private Long paidAmount;

    /**
     * 删除状态：0->未删除；1->已删除
     */
    private Integer isDeleted;

    /**
     * 订单描述
     */
    private String description;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 订单提交时间
     */
    // @TableField(value = "gmt_created", fill = FieldFill.INSERT)
    private Date gmtCreated;

    /**
     * 订单最近更新时间
     */
    // @TableField(value = "gmt_modified", fill = FieldFill.INSERT_UPDATE)
    private Date gmtModified;
}
