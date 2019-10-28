package com.sxdx.kiki.server.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sxdx.kiki.common.entity.system.GeneratorConfig;

/**
 * @author MrBird
 */
public interface IGeneratorConfigService extends IService<GeneratorConfig> {

    /**
     * 查询
     *
     * @return GeneratorConfig
     */
    GeneratorConfig findGeneratorConfig();

    /**
     * 修改
     *
     * @param generatorConfig generatorConfig
     */
    void updateGeneratorConfig(GeneratorConfig generatorConfig);

}
