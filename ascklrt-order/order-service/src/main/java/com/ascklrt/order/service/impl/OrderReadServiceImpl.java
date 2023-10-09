package com.ascklrt.order.service.impl;

import com.ascklrt.order.service.IOrderReadService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author goumang
 * @description
 * @date 2023/2/13 16:50
 */
@DubboService
public class OrderReadServiceImpl implements IOrderReadService {

    @Override
    public String test() {
        return "test123";
    }
}
