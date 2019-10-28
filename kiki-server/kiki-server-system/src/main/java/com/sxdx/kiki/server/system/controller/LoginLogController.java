package com.sxdx.kiki.server.system.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.sxdx.kiki.common.annotation.Log;
import com.sxdx.kiki.common.entity.KikiResponse;
import com.sxdx.kiki.common.entity.QueryRequest;
import com.sxdx.kiki.common.entity.system.LoginLog;
import com.sxdx.kiki.common.exception.KikiException;
import com.sxdx.kiki.common.utils.KikiUtil;
import com.sxdx.kiki.server.system.service.ILoginLogService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("loginLog")
public class LoginLogController {

    @Autowired
    private ILoginLogService loginLogService;

    @GetMapping
    public KikiResponse loginLogList(LoginLog loginLog, QueryRequest request) {
        Map<String, Object> dataTable = KikiUtil.getDataTable(this.loginLogService.findLoginLogs(loginLog, request));
        return new KikiResponse().data(dataTable);
    }

    @GetMapping("/{username}")
    public KikiResponse getUserLastSevenLoginLogs(@NotBlank(message = "{required}") @PathVariable String username) {
        List<LoginLog> userLastSevenLoginLogs = this.loginLogService.findUserLastSevenLoginLogs(username);
        return new KikiResponse().data(userLastSevenLoginLogs);
    }

    @Log("删除登录日志")
    @DeleteMapping("{ids}")
    @PreAuthorize("hasAnyAuthority('loginlog:delete')")
    public void deleteLogss(@NotBlank(message = "{required}") @PathVariable String ids) throws KikiException {
        try {
            String[] loginLogIds = ids.split(StringPool.COMMA);
            this.loginLogService.deleteLoginLogs(loginLogIds);
        } catch (Exception e) {
            String message = "删除登录日志失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }

    @Log("导出登录日志数据")
    @PostMapping("excel")
    @PreAuthorize("hasAnyAuthority('loginlog:export')")
    public void export(QueryRequest request, LoginLog loginLog, HttpServletResponse response) throws KikiException {
        try {
            List<LoginLog> loginLogs = this.loginLogService.findLoginLogs(loginLog, request).getRecords();
            ExcelKit.$Export(LoginLog.class, response).downXlsx(loginLogs, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }
}
