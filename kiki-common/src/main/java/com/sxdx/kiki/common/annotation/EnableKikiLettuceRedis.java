package com.sxdx.kiki.common.annotation;

import com.sxdx.kiki.common.configure.KikiLettuceRedisConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自定义的RedisTemplate,key序列化策略采用StringRedisSerializer
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(KikiLettuceRedisConfigure.class)
public @interface EnableKikiLettuceRedis {
}
