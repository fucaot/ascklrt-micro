package com.ascklrt.infrastructure.framework.spring.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BeanLife implements BeanNameAware, BeanFactoryAware, ApplicationContextAware, InitializingBean {

    private String name;

    private BeanFactory beanFactory;

    private ApplicationContext applicationContext;

    public BeanLife() {
        System.out.println("BeanLife-------------初始化方法执行！");
    }

    @Value("张三")
    public void setName(String name) {
        this.name = name;
    }

    @PostConstruct
    public void init() {
        System.out.println("BeanLife-----------------------init方法执行了！");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        System.out.println("BeanLife-------------setBeanFactory方法执行了！");
    }

    @Override
    public void setBeanName(String name) {
        this.name = name;
        System.out.println("BeanLife-------------setBeanName方法执行了！");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("BeanLife-------------afterPropertiesSet方法执行了！");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        System.out.println("BeanLife-------------setApplicationContext方法执行了！");
    }
}
