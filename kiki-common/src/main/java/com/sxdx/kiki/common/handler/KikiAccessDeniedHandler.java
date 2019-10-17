package com.sxdx.kiki.common.handler;

import com.sxdx.kiki.common.entity.KikiResponse;
import com.sxdx.kiki.common.utils.KikiUtil;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理资源服务器异常:无访问权限异常（403）
 */
public class KikiAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        KikiResponse kikiResponse = new KikiResponse();
        KikiUtil.makeResponse(
                response, MediaType.APPLICATION_JSON_UTF8_VALUE,
                HttpServletResponse.SC_FORBIDDEN, kikiResponse.message("没有权限访问该资源"));
    }
}