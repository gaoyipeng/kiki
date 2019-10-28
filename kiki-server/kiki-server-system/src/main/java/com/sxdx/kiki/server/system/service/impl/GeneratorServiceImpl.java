package com.sxdx.kiki.server.system.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sxdx.kiki.common.entity.KikiConstant;
import com.sxdx.kiki.common.entity.QueryRequest;
import com.sxdx.kiki.common.entity.system.Column;
import com.sxdx.kiki.common.entity.system.Table;
import com.sxdx.kiki.common.utils.SortUtil;
import com.sxdx.kiki.server.system.mapper.GeneratorMapper;
import com.sxdx.kiki.server.system.service.IGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author MrBird
 */
@Service
public class GeneratorServiceImpl implements IGeneratorService {
    @Autowired
    private GeneratorMapper generatorMapper;

    @Override
    public List<String> getDatabases(String databaseType) {
        return generatorMapper.getDatabases(databaseType);
    }

    @Override
    public IPage<Table> getTables(String tableName, QueryRequest request, String databaseType, String schemaName) {
        Page<Table> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "createTime", KikiConstant.ORDER_ASC, false);
        return generatorMapper.getTables(page, tableName, databaseType, schemaName);
    }

    @Override
    public List<Column> getColumns(String databaseType, String schemaName, String tableName) {
        return generatorMapper.getColumns(databaseType, schemaName, tableName);
    }
}
