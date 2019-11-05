package com.sxdx.kiki.gateway.filter;

import com.netflix.zuul.context.RequestContext;
import com.sxdx.kiki.common.entity.KikiResponse;
import com.sxdx.kiki.common.utils.KikiUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * 自定义Zuul异常处理
 */
@Slf4j
@Component
public class KikiGatewayErrorFilter extends SendErrorFilter {

    @Override
    public Object run() {
        try {
            KikiResponse kikiResponse = new KikiResponse();
            RequestContext ctx = RequestContext.getCurrentContext();
            String serviceId = (String) ctx.get(FilterConstants.SERVICE_ID_KEY);

            ExceptionHolder exception = findZuulException(ctx.getThrowable());
            String errorCause = exception.getErrorCause();
            Throwable throwable = exception.getThrowable();
            String message = throwable.getMessage();
            message = StringUtils.isBlank(message) ? errorCause : message;
            kikiResponse = resolveExceptionMessage(message, serviceId, kikiResponse);

            HttpServletResponse response = ctx.getResponse();
            KikiUtil.makeResponse(
                    response, MediaType.APPLICATION_JSON_UTF8_VALUE,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR, kikiResponse
            );
            log.error("Zull sendError：{}", kikiResponse.getMessage());
        } catch (Exception ex) {
            log.error("Zuul sendError", ex);
            ReflectionUtils.rethrowRuntimeException(ex);
        }
        return null;
    }

    private KikiResponse resolveExceptionMessage(String message, String serviceId, KikiResponse kikiResponse) {
        if (StringUtils.containsIgnoreCase(message, "time out")) {
            return kikiResponse.message("请求" + serviceId + "服务超时");
        }
        if (StringUtils.containsIgnoreCase(message, "forwarding error")) {
            return kikiResponse.message(serviceId + "服务不可用");
        }
        return kikiResponse.message("Zuul请求" + serviceId + "服务异常");
    }
}