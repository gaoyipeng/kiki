package com.sxdx.kiki.common.configure;

import com.sxdx.kiki.common.interceptor.KikiServerProtectInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 用于校验网关Zuul Token
 * 注册了一个BCryptPasswordEncoder类型的PasswordEncoderBean
 */
public class KikiServerProtectConfigure implements WebMvcConfigurer {
    @Bean
    public HandlerInterceptor kikiServerProtectInterceptor() {
        return new KikiServerProtectInterceptor();
    }

    /**
     * 加入到了Spring的拦截器链中
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(kikiServerProtectInterceptor());
    }

    /**
     * Spring Security内部实现好的BCryptPasswordEncoder。
     * BCryptPasswordEncoder的特点就是，对于一个相同的密码，每次加密出来的加密串都不同
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(value = PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
