package com.sxdx.kiki.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxdx.kiki.common.entity.system.SystemUser;


public interface UserMapper extends BaseMapper<SystemUser> {
    SystemUser findByName(String username);
}
