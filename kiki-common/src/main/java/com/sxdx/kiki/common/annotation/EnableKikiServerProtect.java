package com.sxdx.kiki.common.annotation;

import com.sxdx.kiki.common.configure.KikiServerProtectConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启微服务防护，避免客户端绕过网关直接请求微服务；
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(KikiServerProtectConfigure.class)
public @interface EnableKikiServerProtect {

}
