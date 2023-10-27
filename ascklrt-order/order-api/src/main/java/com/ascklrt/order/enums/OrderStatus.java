package com.ascklrt.order.enums;

public enum OrderStatus {
    CREATE("订单创建"),
    PAYING("支付中"),
    PAY_FINISH("支付完成"),
    COMPLETE("订单完成"),
    CANCEL("订单取消"),                 // 订单取消一般用于未付款
    CLOSE("订单关闭"),                  // 订单关闭一般用于退款
    ;

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
