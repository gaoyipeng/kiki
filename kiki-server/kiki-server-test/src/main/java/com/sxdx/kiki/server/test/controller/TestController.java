package com.sxdx.kiki.server.test.controller;

import com.sxdx.kiki.common.entity.KikiResponse;
import com.sxdx.kiki.common.entity.QueryRequest;
import com.sxdx.kiki.common.entity.system.SystemUser;
import com.sxdx.kiki.server.test.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

@Slf4j
@RestController
public class TestController {

    @Autowired
    private IUserService userService;

    /**
     * 用于演示 Feign调用受保护的远程方法
     */
    @GetMapping("user/list")
    public KikiResponse getRemoteUserList(QueryRequest request, SystemUser user) {
        String aa = "";
        return userService.userList(request, user);
    }

    /**
     * 用于演示 Feign调用受保护的远程方法
     */
    @GetMapping("aa")
    public String aa() {
        log.info("Feign调用febs-server-system的/aa服务");
        return userService.aa();
    }

    @GetMapping("user")
    public Principal currentUser(Principal principal) {
        return principal;
    }
}
