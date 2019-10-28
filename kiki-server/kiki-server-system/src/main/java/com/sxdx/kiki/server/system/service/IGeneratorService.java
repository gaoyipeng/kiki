package com.sxdx.kiki.server.system.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sxdx.kiki.common.entity.QueryRequest;
import com.sxdx.kiki.common.entity.system.Column;
import com.sxdx.kiki.common.entity.system.Table;

import java.util.List;

/**
 * @author MrBird
 */
public interface IGeneratorService {

    List<String> getDatabases(String databaseType);

    IPage<Table> getTables(String tableName, QueryRequest request, String databaseType, String schemaName);

    List<Column> getColumns(String databaseType, String schemaName, String tableName);
}
