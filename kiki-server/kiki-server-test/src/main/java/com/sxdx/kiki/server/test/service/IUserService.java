package com.sxdx.kiki.server.test.service;

import com.sxdx.kiki.common.entity.KikiResponse;
import com.sxdx.kiki.common.entity.KikiServerConstant;
import com.sxdx.kiki.common.entity.QueryRequest;
import com.sxdx.kiki.common.entity.system.SystemUser;
import com.sxdx.kiki.server.test.service.fallback.UserServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign客户端
 */
@FeignClient(value = KikiServerConstant.KIKI_SERVER_SYSTEM, contextId = "userServiceClient", fallbackFactory = UserServiceFallback.class)
public interface IUserService {

    @GetMapping("user")
    KikiResponse userList(@RequestParam QueryRequest queryRequest, @RequestParam SystemUser user);

    @GetMapping("/aa")
    String aa();
}
