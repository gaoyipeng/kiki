package com.sxdx.kiki.server.system.controller;


import com.sxdx.kiki.common.annotation.Log;
import com.sxdx.kiki.common.entity.KikiResponse;
import com.sxdx.kiki.common.entity.QueryRequest;
import com.sxdx.kiki.common.entity.system.Column;
import com.sxdx.kiki.common.entity.system.GeneratorConfig;
import com.sxdx.kiki.common.entity.system.GeneratorConstant;
import com.sxdx.kiki.common.exception.FileDownloadException;
import com.sxdx.kiki.common.exception.KikiException;
import com.sxdx.kiki.common.utils.FileUtil;
import com.sxdx.kiki.common.utils.KikiUtil;
import com.sxdx.kiki.server.system.helper.GeneratorHelper;
import com.sxdx.kiki.server.system.service.IGeneratorConfigService;
import com.sxdx.kiki.server.system.service.IGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("generator")
public class GeneratorController {

    private static final String SUFFIX = "_code.zip";

    @Autowired
    private IGeneratorService generatorService;
    @Autowired
    private IGeneratorConfigService generatorConfigService;
    @Autowired
    private GeneratorHelper generatorHelper;

    @GetMapping("tables")
    @PreAuthorize("hasAnyAuthority('gen:generate')")
    public KikiResponse tablesInfo(String tableName, QueryRequest request) {
        Map<String, Object> dataTable = KikiUtil.getDataTable(generatorService.getTables(tableName, request, GeneratorConstant.DATABASE_TYPE, GeneratorConstant.DATABASE_NAME));
        return new KikiResponse().data(dataTable);
    }

    @Log("生成代码")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('gen:generate:gen')")
    public void generate(@NotBlank(message = "{required}") String name, String remark, HttpServletResponse response) throws FileDownloadException {
        try {
            GeneratorConfig generatorConfig = generatorConfigService.findGeneratorConfig();
            if (generatorConfig == null) {
                throw new KikiException("代码生成配置为空");
            }

            String className = name;
            if (GeneratorConfig.TRIM_YES.equals(generatorConfig.getIsTrim())) {
                className = RegExUtils.replaceFirst(name, generatorConfig.getTrimValue(), StringUtils.EMPTY);
            }

            generatorConfig.setTableName(name);
            generatorConfig.setClassName(KikiUtil.underscoreToCamel(className));
            generatorConfig.setTableComment(remark);
            // 生成代码到临时目录
            List<Column> columns = generatorService.getColumns(GeneratorConstant.DATABASE_TYPE, GeneratorConstant.DATABASE_NAME, name);
            generatorHelper.generateEntityFile(columns, generatorConfig);
            generatorHelper.generateMapperFile(columns, generatorConfig);
            generatorHelper.generateMapperXmlFile(columns, generatorConfig);
            generatorHelper.generateServiceFile(columns, generatorConfig);
            generatorHelper.generateServiceImplFile(columns, generatorConfig);
            generatorHelper.generateControllerFile(columns, generatorConfig);
            // 打包
            String zipFile = System.currentTimeMillis() + SUFFIX;
            FileUtil.compress(GeneratorConstant.TEMP_PATH + "src", zipFile);
            // 下载
            FileUtil.download(zipFile, name + SUFFIX, true, response);
            // 删除临时目录
            FileUtil.delete(GeneratorConstant.TEMP_PATH);
        } catch (Exception e) {
            String message = "代码生成失败，" + e.getMessage();
            log.error(message, e);
            throw new FileDownloadException(message);
        }
    }
}
