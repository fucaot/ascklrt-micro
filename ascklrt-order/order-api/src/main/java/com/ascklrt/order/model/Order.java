package com.ascklrt.order.model;

import lombok.Data;

import java.util.Date;

@Data
public class Order {

    private String orderNum;

    /**
     * 删除状态：0->未删除；1->已删除
     */
    private Integer isDeleted;

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
