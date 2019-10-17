package com.sxdx.kiki.common.annotation;

import com.sxdx.kiki.common.configure.KikiOAuth2FeignConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启带令牌的Feign请求，避免微服务内部调用出现401异常；
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(KikiOAuth2FeignConfigure.class)
public @interface EnableKikiOauth2FeignClient {

}
