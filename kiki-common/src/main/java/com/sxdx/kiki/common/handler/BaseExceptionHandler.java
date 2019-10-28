package com.sxdx.kiki.common.handler;

import com.sxdx.kiki.common.entity.KikiResponse;
import com.sxdx.kiki.common.exception.KikiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局异常处理类（处理Controller层抛出来的异常）
 */
@Slf4j
public class BaseExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public KikiResponse handleException(Exception e) {
        log.error("系统内部异常，异常信息", e);
        return new KikiResponse().message("系统内部异常");
    }

    @ExceptionHandler(value = KikiException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public KikiResponse handleKikiAuthException(KikiException e) {
        log.error("系统错误", e);
        return new KikiResponse().message(e.getMessage());
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public KikiResponse handleAccessDeniedException(){
        return new KikiResponse().message("没有权限访问该资源");
    }
}
