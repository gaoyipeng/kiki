package com.sxdx.kiki.server.system.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.sxdx.kiki.common.annotation.Log;
import com.sxdx.kiki.common.entity.KikiResponse;
import com.sxdx.kiki.common.entity.QueryRequest;
import com.sxdx.kiki.common.entity.system.Role;
import com.sxdx.kiki.common.exception.KikiException;
import com.sxdx.kiki.common.utils.KikiUtil;
import com.sxdx.kiki.server.system.service.IRoleService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

@Slf4j
@Validated
@RestController
@RequestMapping("role")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    private String message;

    @GetMapping
    public KikiResponse roleList(QueryRequest queryRequest, Role role) {
        Map<String, Object> dataTable = KikiUtil.getDataTable(roleService.findRoles(role, queryRequest));
        return new KikiResponse().data(dataTable);
    }

    @GetMapping("options")
    public KikiResponse roles(){
        List<Role> allRoles = roleService.findAllRoles();
        return new KikiResponse().data(allRoles);
    }

    @GetMapping("check/{roleName}")
    public boolean checkRoleName(@NotBlank(message = "{required}") @PathVariable String roleName) {
        Role result = this.roleService.findByName(roleName);
        return result == null;
    }

    @Log("新增角色")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('role:add')")
    public void addRole(@Valid Role role) throws KikiException {
        try {
            this.roleService.createRole(role);
        } catch (Exception e) {
            message = "新增角色失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }

    @Log("删除角色")
    @DeleteMapping("/{roleIds}")
    @PreAuthorize("hasAnyAuthority('role:delete')")
    public void deleteRoles(@NotBlank(message = "{required}") @PathVariable String roleIds) throws KikiException {
        try {
            String[] ids = roleIds.split(StringPool.COMMA);
            this.roleService.deleteRoles(ids);
        } catch (Exception e) {
            message = "删除角色失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }

    @Log("修改角色")
    @PutMapping
    @PreAuthorize("hasAnyAuthority('role:update')")
    public void updateRole(@Valid Role role) throws KikiException {
        try {
            this.roleService.updateRole(role);
        } catch (Exception e) {
            message = "修改角色失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }

    @Log("导出角色数据")
    @PostMapping("excel")
    @PreAuthorize("hasAnyAuthority('role:export')")
    public void export(QueryRequest queryRequest, Role role, HttpServletResponse response) throws KikiException {
        try {
            List<Role> roles = this.roleService.findRoles(role, queryRequest).getRecords();
            ExcelKit.$Export(Role.class, response).downXlsx(roles, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }
}
