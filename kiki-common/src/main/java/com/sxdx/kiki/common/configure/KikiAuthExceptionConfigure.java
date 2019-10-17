package com.sxdx.kiki.common.configure;

import com.sxdx.kiki.common.handler.KikiAccessDeniedHandler;
import com.sxdx.kiki.common.handler.KikiAuthExceptionEntryPoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @ConditionalOnMissingBean(name = "accessDeniedHandler")作用:
 * 当微服务系统的Spring IOC容器中没有名称为accessDeniedHandler的Bean的时候，
 * 就将KikiAccessDeniedHandler注册为一个Bean。
 * 这样做的好处在于，子系统可以自定义自个儿的资源服务器异常处理器，覆盖我们在kiki-common通用模块里定义的。
 */
public class KikiAuthExceptionConfigure{
    @Bean
    @ConditionalOnMissingBean(name = "accessDeniedHandler")
    public KikiAccessDeniedHandler accessDeniedHandler() {
        return new KikiAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean(name = "authenticationEntryPoint")
    public KikiAuthExceptionEntryPoint authenticationEntryPoint() {
        return new KikiAuthExceptionEntryPoint();
    }
}
