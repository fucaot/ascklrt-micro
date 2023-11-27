package com.ascklrt.infrastructure.framework.spring;

import com.ascklrt.infrastructure.framework.spring.bean.BeanLife;
import com.ascklrt.infrastructure.framework.spring.config.SpringConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class BeanApp {

    public static void maina(String[] args) {
        // 创建bean来查看bean生命中周期
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfig.class);
        BeanLife bean = ctx.getBean(BeanLife.class);
        System.out.println(bean);
    }
}
