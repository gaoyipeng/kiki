package com.sxdx.kiki.server.system.configure;

import com.sxdx.kiki.common.handler.KikiAccessDeniedHandler;
import com.sxdx.kiki.common.handler.KikiAuthExceptionEntryPoint;
import com.sxdx.kiki.server.system.properties.KikiServerSystemProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import javax.annotation.Resource;

@Configuration
@EnableResourceServer
public class KikiServerSystemResourceServerConfigure extends ResourceServerConfigurerAdapter {
    @Resource
    private KikiAccessDeniedHandler accessDeniedHandler;
    @Resource
    private KikiAuthExceptionEntryPoint exceptionEntryPoint;

    @Autowired
    private KikiServerSystemProperties properties;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        String[] anonUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(properties.getAnonUrl(), ",");

        http.csrf().disable()
                .requestMatchers().antMatchers("/**")
                .and()
                .authorizeRequests()
                .antMatchers(anonUrls).permitAll()
                .antMatchers("/**").authenticated();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(exceptionEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
    }
}