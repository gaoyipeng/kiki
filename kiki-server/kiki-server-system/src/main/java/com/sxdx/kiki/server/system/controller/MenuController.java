package com.sxdx.kiki.server.system.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.sxdx.kiki.common.annotation.Log;
import com.sxdx.kiki.common.entity.KikiResponse;
import com.sxdx.kiki.common.entity.router.VueRouter;
import com.sxdx.kiki.common.entity.system.Menu;
import com.sxdx.kiki.common.exception.KikiException;
import com.sxdx.kiki.server.system.service.IMenuService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @GetMapping("/{username}")
    public KikiResponse getUserRouters(@NotBlank(message = "{required}") @PathVariable String username) {
        Map<String, Object> result = new HashMap<>();
        List<VueRouter<Menu>> userRouters = this.menuService.getUserRouters(username);
        String userPermissions = this.menuService.findUserPermissions(username);
        String[] permissionArray = new String[0];
        if (StringUtils.isNoneBlank(userPermissions)) {
            permissionArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(userPermissions, ",");
        }
        result.put("routes", userRouters);
        result.put("permissions", permissionArray);
        return new KikiResponse().data(result);
    }

    @GetMapping
    public KikiResponse menuList(Menu menu) {
        Map<String, Object> menus = this.menuService.findMenus(menu);
        return new KikiResponse().data(menus);
    }

    @GetMapping("/permissions")
    public String findUserPermissions(String username) {
        return this.menuService.findUserPermissions(username);
    }

    @Log("新增菜单/按钮")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('menu:add')")
    public void addMenu(@Valid Menu menu) throws KikiException {
        try {
            this.menuService.createMenu(menu);
        } catch (Exception e) {
            String message = "新增菜单/按钮失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }

    @Log("删除菜单/按钮")
    @DeleteMapping("/{menuIds}")
    @PreAuthorize("hasAnyAuthority('menu:delete')")
    public void deleteMenus(@NotBlank(message = "{required}") @PathVariable String menuIds) throws KikiException {
        try {
            String[] ids = menuIds.split(StringPool.COMMA);
            this.menuService.deleteMeuns(ids);
        } catch (Exception e) {
            String message = "删除菜单/按钮失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }

    @Log("修改菜单/按钮")
    @PutMapping
    @PreAuthorize("hasAnyAuthority('menu:update')")
    public void updateMenu(@Valid Menu menu) throws KikiException {
        try {
            this.menuService.updateMenu(menu);
        } catch (Exception e) {
            String message = "修改菜单/按钮失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }

    @Log("导出菜单数据")
    @PostMapping("excel")
    @PreAuthorize("hasAnyAuthority('menu:export')")
    public void export(Menu menu, HttpServletResponse response) throws KikiException {
        try {
            List<Menu> menus = this.menuService.findMenuList(menu);
            ExcelKit.$Export(Menu.class, response).downXlsx(menus, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }
}