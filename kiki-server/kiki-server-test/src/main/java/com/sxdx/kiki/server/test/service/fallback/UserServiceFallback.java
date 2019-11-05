package com.sxdx.kiki.server.test.service.fallback;


import com.sxdx.kiki.common.annotation.Fallback;
import com.sxdx.kiki.common.entity.KikiResponse;
import com.sxdx.kiki.common.entity.QueryRequest;
import com.sxdx.kiki.common.entity.system.SystemUser;
import com.sxdx.kiki.server.test.service.IUserService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * Feign回退
 *
 * @author MrBird
 */
@Slf4j
@Fallback
public class UserServiceFallback implements FallbackFactory<IUserService> {

    @Override
    public IUserService create(Throwable throwable) {

        return new IUserService() {
            @Override
            public KikiResponse userList(QueryRequest queryRequest, SystemUser user) {
                log.error("获取用户信息失败", throwable);
                return null;
            }

            @Override
            public String aa() {
                return null;
            }

        };
    }
}