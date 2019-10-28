package com.sxdx.kiki.server.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sxdx.kiki.common.entity.KikiConstant;
import com.sxdx.kiki.common.entity.QueryRequest;
import com.sxdx.kiki.common.entity.system.Role;
import com.sxdx.kiki.common.entity.system.RoleMenu;
import com.sxdx.kiki.common.utils.SortUtil;
import com.sxdx.kiki.server.system.mapper.RoleMapper;
import com.sxdx.kiki.server.system.service.IRoleMenuService;
import com.sxdx.kiki.server.system.service.IRoleService;
import com.sxdx.kiki.server.system.service.IUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
@Service("roleService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Autowired
    private IRoleMenuService roleMenuService;
    @Autowired
    private IUserRoleService userRoleService;

    @Override
    public IPage<Role> findRoles(Role role, QueryRequest request) {
        Page<Role> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", KikiConstant.ORDER_DESC, false);
        return this.baseMapper.findRolePage(page, role);
    }

    @Override
    public List<Role> findUserRole(String userName) {
        return baseMapper.findUserRole(userName);
    }

    @Override
    public List<Role> findAllRoles() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Role::getRoleId);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public Role findByName(String roleName) {
        return baseMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleName, roleName));
    }

    @Override
    @Transactional
    public void createRole(Role role) {
        role.setCreateTime(new Date());
        this.save(role);

        String[] menuIds = role.getMenuIds().split(StringPool.COMMA);
        setRoleMenus(role, menuIds);
    }

    @Override
    @Transactional
    public void deleteRoles(String[] roleIds) {
        List<String> list = Arrays.asList(roleIds);
        baseMapper.deleteBatchIds(list);

        this.roleMenuService.deleteRoleMenusByRoleId(roleIds);
        this.userRoleService.deleteUserRolesByRoleId(roleIds);
    }

    @Override
    @Transactional
    public void updateRole(Role role) {
        role.setRoleName(null);
        role.setModifyTime(new Date());
        baseMapper.updateById(role);

        roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, role.getRoleId()));

        String[] menuIds = role.getMenuIds().split(StringPool.COMMA);
        setRoleMenus(role, menuIds);
    }

    private void setRoleMenus(Role role, String[] menuIds) {
        Arrays.stream(menuIds).forEach(menuId -> {
            RoleMenu rm = new RoleMenu();
            if (StringUtils.isNotBlank(menuId)) {
                rm.setMenuId(Long.valueOf(menuId));
            }
            rm.setRoleId(role.getRoleId());
            this.roleMenuService.save(rm);
        });
    }

}
