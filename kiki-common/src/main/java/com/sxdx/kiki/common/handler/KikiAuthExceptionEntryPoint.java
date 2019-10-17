package com.sxdx.kiki.common.handler;

import com.sxdx.kiki.common.entity.KikiResponse;
import com.sxdx.kiki.common.utils.KikiUtil;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理资源服务器异常:token异常处理（401）
 */
public class KikiAuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        KikiResponse kikiResponse = new KikiResponse();
        /*response.setContentType("application/json;charset=UTF-8");
        response.setStatus(401);
        response.getOutputStream().write(JSONObject.toJSONString(kikiResponse.message("token无效")).getBytes());*/

        KikiUtil.makeResponse(
                response, MediaType.APPLICATION_JSON_UTF8_VALUE,
                HttpServletResponse.SC_UNAUTHORIZED, kikiResponse.message("token无效")
        );
    }
}