package com.ascklrt.infrastructure.middle.rocketmq;

import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientConfigurationBuilder;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.message.MessageBuilder;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;

public class ProducerDemo {

    public static void main(String[] args) throws Exception {

        // 接入点地址
        String endPoint = "localhost:8081";

        String topic = "TEST_TOPIC";

        ClientServiceProvider provider = ClientServiceProvider.loadService();

        ClientConfigurationBuilder build = ClientConfiguration.newBuilder().setEndpoints(endPoint);
        ClientConfiguration config = build.build();

        // 初始化发往TEST_TOPIC下testkey的producer 配置需要通信的配置及预绑定topic
        Producer producer = provider.newProducerBuilder()
                .setTopics(topic)
                .setClientConfiguration(config)
                .build();
        Message message = provider.newMessageBuilder()
                .setTopic(topic)
                .setKeys("testKey1")
                .setTag("test")
                .setBody("{\"message\": \"this is test message\"}".getBytes())
                .build();

        try {
            producer.send(message);
            System.out.println("发送成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
