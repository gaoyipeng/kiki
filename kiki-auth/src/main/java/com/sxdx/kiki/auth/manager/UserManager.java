package com.sxdx.kiki.auth.manager;

import com.sxdx.kiki.auth.mapper.MenuMapper;
import com.sxdx.kiki.auth.mapper.UserMapper;
import com.sxdx.kiki.common.entity.system.Menu;
import com.sxdx.kiki.common.entity.system.SystemUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserManager {

    @Resource
    private UserMapper userMapper;
    @Resource
    private MenuMapper menuMapper;

    public SystemUser findByName(String username) {
        return userMapper.findByName(username);
    }

    public String findUserPermissions(String username) {
        List<Menu> userPermissions = menuMapper.findUserPermissions(username);

        List<String> perms = new ArrayList<>();
        for (Menu m : userPermissions) {
            perms.add(m.getPerms());
        }
        return StringUtils.join(perms, ",");
    }
}
