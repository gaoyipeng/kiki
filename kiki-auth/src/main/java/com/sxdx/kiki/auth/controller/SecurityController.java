package com.sxdx.kiki.auth.controller;


import com.sxdx.kiki.auth.service.ValidateCodeService;
import com.sxdx.kiki.common.entity.KikiResponse;
import com.sxdx.kiki.common.exception.KikiException;
import com.sxdx.kiki.common.exception.ValidateCodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@RestController
public class SecurityController {

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @Autowired
    private ValidateCodeService validateCodeService;


    @GetMapping("oauth/test")
    public String testOauth() {
        return "oauth";
    }

    @GetMapping("user")
    public Principal currentUser(Principal principal) {
        return principal;
    }

    @DeleteMapping("signout")
    public KikiResponse signout(HttpServletRequest request) throws KikiException {
        String authorization = request.getHeader("Authorization");
        String token = StringUtils.replace(authorization, "bearer ", "");
        KikiResponse kikiResponse = new KikiResponse();
        if (!consumerTokenServices.revokeToken(token)) {
            throw new KikiException("退出登录失败");
        }
        return kikiResponse.message("退出登录成功");
    }

    /**
     * 获取验证码
     *
     * @param request
     * @param response
     * @throws IOException
     * @throws ValidateCodeException
     */
    @GetMapping("captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException, ValidateCodeException {
        validateCodeService.create(request, response);
    }
}
