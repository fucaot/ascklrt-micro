package com.ascklrt.order.pkg;

import com.ascklrt.order.pkg.enums.Country;
import com.ascklrt.order.pkg.enums.WeightUnit;
import lombok.Data;

/**
 * 包裹实体
 */
@Data
public class Package {

    /**
     * 包裹编码
     */
    private String packageCode;

    /**
     * 包裹编码
     */
    private Country country;

    /**
     * 重量，重量：g
     */
    private Double weight;

    /**
     * 重量，重量：g
     */
    private WeightUnit weightUnit;

    public Double getKgUnit() {
    	switch(weightUnit) {
            case G:
                // 世纪业务中要考虑抹零等重量计算函数，此处不赘述
                return weight / 1000;
            case KG:
                return weight;
		}
        return null;
	}
}
