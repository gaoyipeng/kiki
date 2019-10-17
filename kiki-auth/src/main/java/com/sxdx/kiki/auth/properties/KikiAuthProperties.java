package com.sxdx.kiki.auth.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@Data
@SpringBootConfiguration
@PropertySource(value = {"classpath:properties/kiki-auth.properties"})
@ConfigurationProperties(prefix = "kiki.auth")
public class KikiAuthProperties {
    private KikiClientsProperties[] clients = {};
    private int accessTokenValiditySeconds = 60 * 60 * 24;
    private int refreshTokenValiditySeconds = 60 * 60 * 24 * 7;

    //验证码配置类
    private KikiValidateCodeProperties code = new KikiValidateCodeProperties();
    // 免认证路径
    private String anonUrl;
}
