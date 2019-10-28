package com.sxdx.kiki.server.system.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sxdx.kiki.common.entity.QueryRequest;
import com.sxdx.kiki.common.entity.system.Role;

import java.util.List;

public interface IRoleService extends IService<Role> {

    IPage<Role> findRoles(Role role, QueryRequest request);

    List<Role> findUserRole(String userName);

    List<Role> findAllRoles();

    Role findByName(String roleName);

    void createRole(Role role);

    void deleteRoles(String[] roleIds) throws Exception;

    void updateRole(Role role) throws Exception;
}
