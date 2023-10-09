package com.ascklrt.order;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author goumang
 * @description
 * @date 2023/2/13 11:30
 */
@EnableDubbo
// @PropertySource(value = "classpath:/provider-config.properties")
@SpringBootApplication
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
