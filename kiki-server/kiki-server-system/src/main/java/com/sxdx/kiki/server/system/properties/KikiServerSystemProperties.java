package com.sxdx.kiki.server.system.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 *
 */
@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:kiki-server-system.properties"})
@ConfigurationProperties(prefix = "kiki.server.system")
public class KikiServerSystemProperties {
    /**
     * 免认证 URI，多个值的话以逗号分隔
     */
    private String anonUrl;

    /**
     * swagger配置
     */
    private KikiSwaggerProperties swagger = new KikiSwaggerProperties();
}
