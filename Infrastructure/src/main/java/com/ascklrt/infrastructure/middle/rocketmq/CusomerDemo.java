package com.ascklrt.infrastructure.middle.rocketmq;

import org.apache.rocketmq.client.apis.ClientConfiguration;

public class CusomerDemo {

    public static void main(String[] args) {

        String endPoint = "localhost:8081";

        ClientConfiguration configuration = ClientConfiguration.newBuilder().setEndpoints(endPoint).build();

        // * 代表接收全部消息
        String tag = "*";


    }
}
