package com.sxdx.kiki.server.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxdx.kiki.common.entity.system.RoleMenu;

import java.util.List;

public interface IRoleMenuService extends IService<RoleMenu> {

    void deleteRoleMenusByRoleId(String[] roleIds);

    void deleteRoleMenusByMenuId(String[] menuIds);

    List<RoleMenu> getRoleMenusByRoleId(String roleId);
}
