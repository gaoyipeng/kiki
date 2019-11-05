package com.sxdx.kiki.register.configure;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * spring-cloud-security 配置，保护eureka服务
 */
@EnableWebSecurity
public class KikiRegisterWebSecurityConfigure  extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/eureka/**")
        .and()
        .authorizeRequests().antMatchers("/actuator/**").permitAll();
        super.configure(http);
    }
}