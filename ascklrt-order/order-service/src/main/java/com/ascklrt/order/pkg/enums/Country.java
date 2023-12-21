package com.ascklrt.order.pkg.enums;

import com.ascklrt.order.pkg.Package;
import com.ascklrt.order.pkg.calculate.PriceCalculate;
import com.ascklrt.order.pkg.calculate.RUPriceCalculate;
import com.ascklrt.order.pkg.calculate.USPriceCalculate;

import java.math.BigDecimal;

/**
 * 国家枚举
 */
public enum Country {

    RU(new RUPriceCalculate()),
    US(new USPriceCalculate()),
    FR((pkg) -> new BigDecimal(pkg.getKgUnit())
            .multiply(new BigDecimal(3.5))
            .add(new BigDecimal(2.5))
            .multiply(new BigDecimal(100))
            .longValue()
    ),
    ;

    private final PriceCalculate priceCalculate;

    Country(PriceCalculate priceCalculate) {
        this.priceCalculate = priceCalculate;
    }

    public PriceCalculate getPriceCalculate(Package pkg) {
        return priceCalculate;
    }
}
