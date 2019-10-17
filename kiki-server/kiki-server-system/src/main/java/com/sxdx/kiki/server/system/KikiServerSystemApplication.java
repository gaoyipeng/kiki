package com.sxdx.kiki.server.system;


import com.sxdx.kiki.common.annotation.KikiCloudApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableDiscoveryClient
@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启Spring Cloud Security权限注解
@KikiCloudApplication
public class KikiServerSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(KikiServerSystemApplication.class, args);
    }

}
