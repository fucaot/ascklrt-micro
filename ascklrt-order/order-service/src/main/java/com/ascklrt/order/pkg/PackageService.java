package com.ascklrt.order.pkg;

public class PackageService {

    /**
     * @param pkg package
     * @return 运费价格，单位分
     */
    Long priceCalculate(Package pkg) {
        return pkg.getCountry().getPriceCalculate(pkg).calculate(pkg);
    }
}
