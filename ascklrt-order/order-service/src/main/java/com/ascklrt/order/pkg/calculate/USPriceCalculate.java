package com.ascklrt.order.pkg.calculate;

import java.math.BigDecimal;

/**
 * 美国计算函数
 */
public class USPriceCalculate implements PriceCalculate{

    @Override
    public Long calculate(com.ascklrt.order.pkg.Package pkg) {
        Double kgUnit = pkg.getKgUnit();
        // 计算
        BigDecimal price = new BigDecimal(kgUnit)
                .multiply(new BigDecimal(3.5))
                .add(new BigDecimal(2.5));

        // 转分
        return price.multiply(new BigDecimal(100)).longValue();
    }

}