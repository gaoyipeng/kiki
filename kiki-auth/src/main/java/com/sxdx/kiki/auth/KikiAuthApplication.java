package com.sxdx.kiki.auth;

import com.sxdx.kiki.common.annotation.EnableKikiLettuceRedis;
import com.sxdx.kiki.common.annotation.KikiCloudApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@KikiCloudApplication
@EnableKikiLettuceRedis
@MapperScan("com.sxdx.kiki.auth.mapper")
public class KikiAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(KikiAuthApplication.class, args);
    }

}
