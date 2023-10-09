package com.ascklrt.web.config;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.CustomizableTraceInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author goumang
 * @description
 * @date 2023/2/14 14:28
 */

@EnableAspectJAutoProxy(proxyTargetClass = true)
@Configuration
public class TraceAopConfig {

    /**
     * 使用注解方式，
     * com.ascklrt.web.*.controller ..(前包和子包) *（类）.*(..)（方法）
     * 或：
     * '@Pointcut("execution(* com.ascklrt.api.*.*(..))")'
     * 或：被注解方法类的所有方法
     * "@within(com.ascklrt.common.annotation.DescriptionLog)"
     * 或：被注解的方法
     * Pointcut("@annotation(com.ascklrt.common.annotation.DescriptionLog)")
     *
     * @param
     * @return void
     * @author goumang
     * @date 2022/4/3 5:40 下午
     */
    @Pointcut("execution(* com.ascklrt.web.*.controller..*.*(..))")
    public void monitor() {}

    @Bean
    public Advisor traceAdvisor() {
        CustomizableTraceInterceptor customizableTraceInterceptor = new CustomizableTraceInterceptor();

        // 步入方法打印输入
        customizableTraceInterceptor.setEnterMessage("Enter ClassName = "
                + CustomizableTraceInterceptor.PLACEHOLDER_TARGET_CLASS_NAME
                + ": "
                + CustomizableTraceInterceptor.PLACEHOLDER_METHOD_NAME
                + "("
                + CustomizableTraceInterceptor.PLACEHOLDER_ARGUMENTS
                + ")"
        );

        // 步出方法打印输出
        customizableTraceInterceptor.setExitMessage("Leaving ="
                + CustomizableTraceInterceptor.PLACEHOLDER_METHOD_NAME
                + "() "
                + CustomizableTraceInterceptor.PLACEHOLDER_RETURN_VALUE
        );

        customizableTraceInterceptor.setUseDynamicLogger(true);
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("com.ascklrt.web.config.TraceAopConfig.monitor()");

        // 在怎样的切面上实现怎样的功能
        return new DefaultPointcutAdvisor(pointcut, customizableTraceInterceptor);
    }
}
