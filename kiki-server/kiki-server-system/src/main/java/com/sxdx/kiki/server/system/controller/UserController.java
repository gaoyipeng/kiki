package com.sxdx.kiki.server.system.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.sxdx.kiki.common.annotation.Log;
import com.sxdx.kiki.common.entity.KikiResponse;
import com.sxdx.kiki.common.entity.QueryRequest;
import com.sxdx.kiki.common.entity.system.LoginLog;
import com.sxdx.kiki.common.entity.system.SystemUser;
import com.sxdx.kiki.common.exception.KikiException;
import com.sxdx.kiki.common.utils.KikiUtil;
import com.sxdx.kiki.server.system.service.ILoginLogService;
import com.sxdx.kiki.server.system.service.IUserService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
@RequestMapping("user")
public class UserController {

    @Autowired
    private IUserService userService;
    @Autowired
    private ILoginLogService loginLogService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("success/{username}")
    public void loginSuccess(@NotBlank(message = "{required}") @PathVariable String username, HttpServletRequest request) {
        // update last login time
        this.userService.updateLoginTime(username);
        // save login log
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setSystemBrowserInfo(request.getHeader("user-agent"));
        this.loginLogService.saveLoginLog(loginLog);
    }

    @GetMapping("index/{username}")
    public KikiResponse index(@NotBlank(message = "{required}") @PathVariable String username) {
        Map<String, Object> data = new HashMap<>();
        // 获取系统访问记录
        Long totalVisitCount = loginLogService.findTotalVisitCount();
        data.put("totalVisitCount", totalVisitCount);
        Long todayVisitCount = loginLogService.findTodayVisitCount();
        data.put("todayVisitCount", todayVisitCount);
        Long todayIp = loginLogService.findTodayIp();
        data.put("todayIp", todayIp);
        // 获取近期系统访问记录
        List<Map<String, Object>> lastTenVisitCount = loginLogService.findLastTenDaysVisitCount(null);
        data.put("lastTenVisitCount", lastTenVisitCount);
        SystemUser param = new SystemUser();
        param.setUsername(username);
        List<Map<String, Object>> lastTenUserVisitCount = loginLogService.findLastTenDaysVisitCount(param);
        data.put("lastTenUserVisitCount", lastTenUserVisitCount);
        return new KikiResponse().data(data);
    }


    @GetMapping
    @PreAuthorize("hasAnyAuthority('user:view')")
    public KikiResponse userList(QueryRequest queryRequest, SystemUser user) {
        Map<String, Object> dataTable = KikiUtil.getDataTable(userService.findUserDetail(user, queryRequest));
        return new KikiResponse().data(dataTable);
    }

    @GetMapping("check/{username}")
    public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String username) {
        return this.userService.findByName(username) == null;
    }

    @Log("新增用户")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('user:add')")
    public void addUser(@Valid SystemUser user) throws KikiException {
        try {
            this.userService.createUser(user);
        } catch (Exception e) {
            String message = "新增用户失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }

    @Log("新增用户")
    @PutMapping
    @PreAuthorize("hasAnyAuthority('user:update')")
    public void updateUser(@Valid SystemUser user) throws KikiException {
        try {
            this.userService.updateUser(user);
        } catch (Exception e) {
            String message = "修改用户失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }

    @DeleteMapping("/{userIds}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public void deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) throws KikiException {
        try {
            String[] ids = userIds.split(StringPool.COMMA);
            this.userService.deleteUsers(ids);
        } catch (Exception e) {
            String message = "删除用户失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }

    @PutMapping("profile")
    public void updateProfile(@Valid SystemUser user) throws KikiException {
        try {
            this.userService.updateProfile(user);
        } catch (Exception e) {
            String message = "修改个人信息失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }

    @PutMapping("avatar")
    public void updateAvatar(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String avatar) throws KikiException {
        try {
            this.userService.updateAvatar(username, avatar);
        } catch (Exception e) {
            String message = "修改头像失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }

    @GetMapping("password/check")
    public boolean checkPassword(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password) {
        SystemUser user = userService.findByName(username);
        return user != null && passwordEncoder.matches(password, user.getPassword());
    }

    @PutMapping("password")
    public void updatePassword(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password) throws KikiException {
        try {
            userService.updatePassword(username, password);
        } catch (Exception e) {
            String message = "修改密码失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }

    @PutMapping("password/reset")
    @PreAuthorize("hasAnyAuthority('user:reset')")
    public void resetPassword(@NotBlank(message = "{required}") String usernames) throws KikiException {
        try {
            String[] usernameArr = usernames.split(StringPool.COMMA);
            this.userService.resetPassword(usernameArr);
        } catch (Exception e) {
            String message = "重置用户密码失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }

    @Log("导出用户数据")
    @PostMapping("excel")
    @PreAuthorize("hasAnyAuthority('user:export')")
    public void export(QueryRequest queryRequest, SystemUser user, HttpServletResponse response) throws KikiException {
        try {
            List<SystemUser> users = this.userService.findUserDetail(user, queryRequest).getRecords();
            ExcelKit.$Export(SystemUser.class, response).downXlsx(users, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }
}
