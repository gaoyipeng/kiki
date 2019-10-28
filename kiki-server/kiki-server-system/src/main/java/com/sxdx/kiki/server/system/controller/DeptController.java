package com.sxdx.kiki.server.system.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.sxdx.kiki.common.annotation.Log;
import com.sxdx.kiki.common.entity.KikiResponse;
import com.sxdx.kiki.common.entity.QueryRequest;
import com.sxdx.kiki.common.entity.system.Dept;
import com.sxdx.kiki.common.exception.KikiException;
import com.sxdx.kiki.server.system.service.IDeptService;
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
@RequestMapping("dept")
public class DeptController {

    @Autowired
    private IDeptService deptService;

    @GetMapping
    public KikiResponse deptList(QueryRequest request, Dept dept) {
        Map<String, Object> depts = this.deptService.findDepts(request, dept);
        return new KikiResponse().data(depts);
    }

    @Log("新增部门")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('dept:add')")
    public void addDept(@Valid Dept dept) throws KikiException {
        try {
            this.deptService.createDept(dept);
        } catch (Exception e) {
            String message = "新增部门失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }

    @Log("删除部门")
    @DeleteMapping("/{deptIds}")
    @PreAuthorize("hasAnyAuthority('dept:delete')")
    public void deleteDepts(@NotBlank(message = "{required}") @PathVariable String deptIds) throws KikiException {
        try {
            String[] ids = deptIds.split(StringPool.COMMA);
            this.deptService.deleteDepts(ids);
        } catch (Exception e) {
            String message = "删除部门失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }

    @Log("修改部门")
    @PutMapping
    @PreAuthorize("hasAnyAuthority('dept:update')")
    public void updateDept(@Valid Dept dept) throws KikiException {
        try {
            this.deptService.updateDept(dept);
        } catch (Exception e) {
            String message = "修改部门失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }

    @Log("导出部门数据")
    @PostMapping("excel")
    @PreAuthorize("hasAnyAuthority('dept:export')")
    public void export(Dept dept, QueryRequest request, HttpServletResponse response) throws KikiException {
        try {
            List<Dept> depts = this.deptService.findDepts(dept, request);
            ExcelKit.$Export(Dept.class, response).downXlsx(depts, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }
}
