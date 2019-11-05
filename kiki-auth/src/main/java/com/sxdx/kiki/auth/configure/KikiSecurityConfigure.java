package com.sxdx.kiki.auth.configure;

import com.sxdx.kiki.auth.filter.ValidateCodeFilter;
import com.sxdx.kiki.auth.service.KikiUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.annotation.Resource;

/*
 * WebSecurity配置
 */
@Order(2)
@EnableWebSecurity
public class KikiSecurityConfigure extends WebSecurityConfigurerAdapter {

    @Autowired
    private KikiUserDetailService userDetailService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ValidateCodeFilter validateCodeFilter;

    /**
     * Spring Security内部实现好的BCryptPasswordEncoder。
     * BCryptPasswordEncoder的特点就是，对于一个相同的密码，每次加密出来的加密串都不同
     *
     * BCryptPasswordEncoder的注册改到common的 KikiServerProtectConfigure里面了
     * @return
     */
    /*@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/

    /**
     * OAuth是一种用来规范令牌（Token）发放的授权机制，目前最新版本为2.0，
     * 其主要包含了四种授权模式：授权码模式、简化模式、密码模式和客户端模式。
     * Spring Cloud OAuth对这四种授权模式进行了实现。
     * <p>
     * 密码模式用到了AuthenticationManager
     *
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .requestMatchers()
                .antMatchers("/oauth/**")
                .and()
                .authorizeRequests()
                .antMatchers("/oauth/**").authenticated()
                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder);
    }

}
