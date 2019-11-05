package com.sxdx.kiki.server.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @GetMapping("/aa")
    public String aa(){
        log.info("/aa服务被调用");
        return "asfasdfdsa";
    }
}
