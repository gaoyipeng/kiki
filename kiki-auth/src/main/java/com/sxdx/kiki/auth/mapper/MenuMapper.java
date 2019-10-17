package com.sxdx.kiki.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sxdx.kiki.common.entity.system.Menu;

import java.util.List;


public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> findUserPermissions(String username);
}