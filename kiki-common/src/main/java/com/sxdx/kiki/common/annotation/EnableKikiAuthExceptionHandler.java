package com.sxdx.kiki.common.annotation;


import com.sxdx.kiki.common.configure.KikiAuthExceptionConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 认证类型异常翻译
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(KikiAuthExceptionConfigure.class)
public @interface EnableKikiAuthExceptionHandler {

}
