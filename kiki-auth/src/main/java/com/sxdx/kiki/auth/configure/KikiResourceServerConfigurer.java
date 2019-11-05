package com.sxdx.kiki.auth.configure;

import com.sxdx.kiki.auth.properties.KikiAuthProperties;
import com.sxdx.kiki.common.handler.KikiAccessDeniedHandler;
import com.sxdx.kiki.common.handler.KikiAuthExceptionEntryPoint;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import javax.annotation.Resource;

/**
 * 资源服务器配置
 **/
@Configuration
@EnableResourceServer
public class KikiResourceServerConfigurer extends ResourceServerConfigurerAdapter {
    @Resource
    private KikiAccessDeniedHandler accessDeniedHandler;
    @Resource
    private KikiAuthExceptionEntryPoint exceptionEntryPoint;
    @Autowired
    private KikiAuthProperties properties;


    @Override
    public void configure(HttpSecurity http) throws Exception {
        String[] anonUrls = StringUtils.splitByWholeSeparatorPreserveAllTokens(properties.getAnonUrl(), ",");

        http.csrf().disable()
                .requestMatchers().antMatchers("/**")
                .and()
                .authorizeRequests()
                .antMatchers(anonUrls).permitAll()
                .antMatchers("/**").authenticated()
                .and().httpBasic();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(exceptionEntryPoint)
                .accessDeniedHandler(accessDeniedHandler);
    }
}
