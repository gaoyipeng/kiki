package com.sxdx.kiki.server.system;


import com.sxdx.kiki.common.annotation.KikiCloudApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@EnableDiscoveryClient
@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true) //开启Spring Cloud Security权限注解
@KikiCloudApplication
@EnableTransactionManagement
@MapperScan("com.sxdx.kiki.server.system.mapper")
@EnableFeignClients
public class KikiServerSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(KikiServerSystemApplication.class, args);
    }

}
