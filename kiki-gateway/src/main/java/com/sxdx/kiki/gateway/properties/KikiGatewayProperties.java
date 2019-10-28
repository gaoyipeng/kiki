package com.sxdx.kiki.gateway.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;


@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:kiki-gateway.properties"})
@ConfigurationProperties(prefix = "kiki.gateway")
public class KikiGatewayProperties {
    /**
     * 禁止外部访问的 URI，多个值的话以逗号分隔
     */
    private String forbidRequestUri;
}
