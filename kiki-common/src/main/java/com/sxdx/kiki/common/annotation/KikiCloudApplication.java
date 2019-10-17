package com.sxdx.kiki.common.annotation;

import com.sxdx.kiki.common.selector.KikiCloudApplicationSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * EnableKikiOauth2FeignClient,EnableKikiServerProtect,KikiCloudApplication 的总集
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(KikiCloudApplicationSelector.class)
public @interface KikiCloudApplication {
}
