package com.sxdx.kiki.common.annotation;

import com.sxdx.kiki.common.configure.JwtTokenEnhancerConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * jwt 自定义Claims
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(JwtTokenEnhancerConfigure.class)
public @interface EnableJwtTokenEnhancer {
}
