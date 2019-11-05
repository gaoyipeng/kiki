package com.sxdx.kiki.auth.service;


import com.sxdx.kiki.auth.manager.UserManager;
import com.sxdx.kiki.common.entity.KikiAuthUser;
import com.sxdx.kiki.common.entity.system.SystemUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class KikiUserDetailService implements UserDetailsService {

    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private UserManager userManager;

    /**
     * 返回一个UserDetails对象，该对象也是一个接口，包含一些用于描述用户信息的方法,包含如下：
     * <p>
     * getAuthorities获取用户包含的权限，返回权限集合，权限是一个继承了GrantedAuthority的对象；
     * getPassword和getUsername用于获取密码和用户名；
     * isAccountNonExpired方法返回boolean类型，用于判断账户是否未过期，未过期返回true反之返回false；
     * isAccountNonLocked方法用于判断账户是否未锁定；
     * isCredentialsNonExpired用于判断用户凭证是否没过期，即密码是否未过期；
     * isEnabled方法用于判断用户是否可用。
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemUser systemUser = userManager.findByName(username);
        if (systemUser != null) {
            String permissions = userManager.findUserPermissions(systemUser.getUsername());
            boolean notLocked = false;
            if (StringUtils.equals(SystemUser.STATUS_VALID, systemUser.getStatus()))
                notLocked = true;
            KikiAuthUser authUser = new KikiAuthUser(systemUser.getUsername(), systemUser.getPassword(),
                    true, true, true, notLocked,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));
            BeanUtils.copyProperties(systemUser, authUser);
            return authUser;
        } else {
            throw new UsernameNotFoundException("");
        }
    }

}