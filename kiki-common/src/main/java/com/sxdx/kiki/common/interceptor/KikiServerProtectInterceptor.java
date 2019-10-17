package com.sxdx.kiki.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.sxdx.kiki.common.entity.KikiConstant;
import com.sxdx.kiki.common.entity.KikiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 校验Zuul Token
 */
public class KikiServerProtectInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 从请求头中获取 Zuul Token
        String token = request.getHeader(KikiConstant.ZUUL_TOKEN_HEADER);
        String zuulToken = new String(Base64Utils.encode(KikiConstant.ZUUL_TOKEN_VALUE.getBytes()));
        // 校验 Zuul Token的正确性
        if (StringUtils.equals(zuulToken, token)) {
            return true;
        } else {
            KikiResponse kikiResponse = new KikiResponse();
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write(JSONObject.toJSONString(kikiResponse.message("请通过网关获取资源")));
            return false;
        }
    }
}
