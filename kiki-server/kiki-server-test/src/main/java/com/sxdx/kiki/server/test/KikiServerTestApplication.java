package com.sxdx.kiki.server.test;


import com.sxdx.kiki.common.annotation.KikiCloudApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;


@EnableFeignClients
@EnableDiscoveryClient
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启Spring Cloud Security权限注解
@SpringBootApplication
@KikiCloudApplication
public class KikiServerTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(KikiServerTestApplication.class, args);
    }

}
