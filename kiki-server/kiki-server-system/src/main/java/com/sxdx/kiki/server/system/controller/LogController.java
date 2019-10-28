package com.sxdx.kiki.server.system.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.sxdx.kiki.common.entity.KikiResponse;
import com.sxdx.kiki.common.entity.QueryRequest;
import com.sxdx.kiki.common.entity.system.Log;
import com.sxdx.kiki.common.exception.KikiException;
import com.sxdx.kiki.common.utils.KikiUtil;
import com.sxdx.kiki.server.system.service.ILogService;
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
@RequestMapping("log")
public class LogController {

    @Autowired
    private ILogService logService;

    @GetMapping
    public KikiResponse logList(Log log, QueryRequest request) {
        Map<String, Object> dataTable = KikiUtil.getDataTable(this.logService.findLogs(log, request));
        return new KikiResponse().data(dataTable);
    }

    @DeleteMapping("{ids}")
    @PreAuthorize("hasAnyAuthority('log:delete')")
    public void deleteLogss(@NotBlank(message = "{required}") @PathVariable String ids) throws KikiException {
        try {
            String[] logIds = ids.split(StringPool.COMMA);
            this.logService.deleteLogs(logIds);
        } catch (Exception e) {
            String message = "删除日志失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }


    @PostMapping("excel")
    @PreAuthorize("hasAnyAuthority('log:export')")
    public void export(QueryRequest request, Log lg, HttpServletResponse response) throws KikiException {
        try {
            List<Log> logs = this.logService.findLogs(lg, request).getRecords();
            ExcelKit.$Export(Log.class, response).downXlsx(logs, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }
}
