package com.sxdx.kiki.server.system.controller;

import com.sxdx.kiki.common.annotation.Log;
import com.sxdx.kiki.common.entity.KikiResponse;
import com.sxdx.kiki.common.entity.system.GeneratorConfig;
import com.sxdx.kiki.common.exception.KikiException;
import com.sxdx.kiki.server.system.service.IGeneratorConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("generatorConfig")
public class GeneratorConfigController {

    @Autowired
    private IGeneratorConfigService generatorConfigService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('gen:config')")
    public KikiResponse getGeneratorConfig() {
        return new KikiResponse().data(generatorConfigService.findGeneratorConfig());
    }

    @Log("修改生成代码配置")
    @PostMapping
    @PreAuthorize("hasAnyAuthority('gen:config:update')")
    public void updateGeneratorConfig(@Valid GeneratorConfig generatorConfig) throws KikiException {
        try {
            if (StringUtils.isBlank(generatorConfig.getId()))
                throw new KikiException("配置id不能为空");
            this.generatorConfigService.updateGeneratorConfig(generatorConfig);
        } catch (Exception e) {
            String message = "修改GeneratorConfig失败";
            log.error(message, e);
            throw new KikiException(message);
        }
    }
}
