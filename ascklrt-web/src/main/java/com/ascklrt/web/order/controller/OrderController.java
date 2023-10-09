package com.ascklrt.web.order.controller;

import com.ascklrt.order.service.IOrderReadService;
import com.ascklrt.order.service.IStreamService;
import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author goumang
 * @description
 * @date 2023/2/14 10:53
 */
@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @DubboReference
    private IOrderReadService orderReadService;

    @DubboReference
    private IStreamService streamService;

    @RequestMapping(value = "/getOrder", method = RequestMethod.GET)
    public String getOrder() {
        String test = orderReadService.test();
        return test;
    }

    public void streamCall() {
        streamService.streamCall("207445637918875",
                new StreamObserver() {

                    @Override
                    public void onNext(Object data) {
                        // 每阶段处理
                        stage(data);
                    }

                    @Override
                    public void onError(Throwable throwable) {}

                    @Override
                    public void onCompleted() {}
                }
        );
    }

    void stage(Object data) {}

}
