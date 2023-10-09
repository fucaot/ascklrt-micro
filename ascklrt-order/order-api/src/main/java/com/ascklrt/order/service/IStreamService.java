package com.ascklrt.order.service;

import org.apache.dubbo.common.stream.StreamObserver;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author goumang
 * @description
 * @date 2023/2/14 17:52
 */
@DubboService
public interface IStreamService {

    Object streamCall(String sn, StreamObserver<String> response);
}
