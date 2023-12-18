package com.ascklrt.infrastructure.middle.rocketmq;

import cn.hutool.json.JSONUtil;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.PushConsumer;

import java.util.Collections;

public class CusomerDemo {

    public static void main(String[] args) throws Exception {
        String endPoint = "localhost:8081";


        ClientServiceProvider provider = ClientServiceProvider.loadService();
        ClientConfiguration configuration = ClientConfiguration.newBuilder().setEndpoints(endPoint).build();

        /**
         * 创建过滤器
         * * 代表接收全部消息
         */
        String tag = "*";
        FilterExpression filterExpression = new FilterExpression(tag, FilterExpressionType.TAG);

        // 为消费者指定所属消费者分组
        String consumer = "TestConsumerGroup";

        // 指定TOPIC
        String topic = "TEST_TOPIC";

        /**
         * 接收TEST_TOPIC下 * 的消息
         */
        PushConsumer pushconsumer = provider.newPushConsumerBuilder()
                .setClientConfiguration(configuration)
                .setConsumerGroup(consumer)
                .setSubscriptionExpressions(Collections.singletonMap(topic, filterExpression))
                .setMessageListener(messageView -> {
                    System.out.println("messageview: " + JSONUtil.toJsonStr(messageView));
                    return ConsumeResult.SUCCESS;
                })
                .build();
        Thread.sleep(Long.MAX_VALUE);
        pushconsumer.close();
    }
}
