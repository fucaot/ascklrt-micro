package com.ascklrt.infrastructure.framework.spring.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * BeanPostProcessor用于增强
 */
@Component
public class MyBeanPostprocessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 进行初始化之前增强
        if (beanName.equals("beanLife")) {
            System.out.println("MyBeanPostprocessor-------------postProcessBeforeInitialization方法执行！");
        }
        return bean;
        // return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 进行初始化之后增强
        if (beanName.equals("beanLife")) {
            System.out.println("postProcessAfterInitialization-------------Mylife读喜庆初始化化方法后开始增强！");

            // cglib动态代理实现增强
            Enhancer enhancer = new Enhancer();

            // 设置需要增强的类
            enhancer.setSuperclass(bean.getClass());

            // 执行回调方法，增强方法
            enhancer.setCallback(new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return method.invoke(method, new Object());
                }
            });
            return enhancer.create();
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
