package com.sxdx.kiki.server.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sxdx.kiki.common.entity.system.UserRole;

import java.util.List;

public interface IUserRoleService extends IService<UserRole> {

	void deleteUserRolesByRoleId(String[] roleIds);

	void deleteUserRolesByUserId(String[] userIds);

	List<String> findUserIdsByRoleId(String[] roleIds);
}
