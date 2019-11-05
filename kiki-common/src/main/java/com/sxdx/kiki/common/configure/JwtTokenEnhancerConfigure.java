package com.sxdx.kiki.common.configure;

import com.sxdx.kiki.common.entity.jwt.JwtTokenEnhancer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

/**
 * 扩展 jwt Claims
 */
public class JwtTokenEnhancerConfigure {
    @Bean
    @ConditionalOnMissingBean(name = "jwtTokenEnhancer")
    public TokenEnhancer jwtTokenEnhancer(){
        return new JwtTokenEnhancer();
    }
}
