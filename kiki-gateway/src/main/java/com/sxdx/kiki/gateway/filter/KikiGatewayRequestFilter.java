package com.sxdx.kiki.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.sxdx.kiki.common.entity.KikiConstant;
import com.sxdx.kiki.common.entity.KikiResponse;
import com.sxdx.kiki.common.utils.KikiUtil;
import com.sxdx.kiki.gateway.properties.KikiGatewayProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Base64Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 微服务防护（必须通过网关访问）
 */
@Slf4j
@Component
public class KikiGatewayRequestFilter extends ZuulFilter {
    @Autowired
    private KikiGatewayProperties properties;
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 对应Zuul生命周期的四个阶段：pre、post、route和error，我们要在请求转发出去前添加请求头，所以这里指定为pre；
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * PreDecorationFilter(处理请求上下文供后续使用)过滤器的优先级为5，
     * 所以我们可以指定为6让我们的过滤器优先级比它低
     * @return
     */
    @Override
    public int filterOrder() {
        return 6;
    }

    /**
     * 方法返回boolean类型，true时表示是否执行该过滤器的run方法，false则表示不执行；
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;//
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        String serviceId = (String) ctx.get(FilterConstants.SERVICE_ID_KEY);
        HttpServletRequest request = ctx.getRequest();
        String host = request.getRemoteHost();
        String method = request.getMethod();
        String uri = request.getRequestURI();

        log.info("请求URI：{}，HTTP Method：{}，请求IP：{}，ServerId：{}", uri, method, host, serviceId);

        // 禁止外部访问资源实现
        boolean shouldForward = true;
        String forbidRequestUri = properties.getForbidRequestUri();
        String[] forbidRequestUris = StringUtils.splitByWholeSeparatorPreserveAllTokens(forbidRequestUri, ",");
        if (forbidRequestUris != null && ArrayUtils.isNotEmpty(forbidRequestUris)) {
            for (String u : forbidRequestUris) {
                if (pathMatcher.match(u, uri)) {
                    shouldForward = false;
                }
            }
        }
        if (!shouldForward) {
            HttpServletResponse response = ctx.getResponse();
            KikiResponse kikiResponse = new KikiResponse().message("该URI不允许外部访问");
            try {
                KikiUtil.makeResponse(
                        response, MediaType.APPLICATION_JSON_UTF8_VALUE,
                        HttpServletResponse.SC_FORBIDDEN, kikiResponse
                );
                ctx.setSendZuulResponse(false);
                ctx.setResponse(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        //网关转发请求前添加Zuul Token
        byte[] token = Base64Utils.encode((KikiConstant.ZUUL_TOKEN_VALUE).getBytes());
        ctx.addZuulRequestHeader(KikiConstant.ZUUL_TOKEN_HEADER, new String(token));
        return null;
    }
}