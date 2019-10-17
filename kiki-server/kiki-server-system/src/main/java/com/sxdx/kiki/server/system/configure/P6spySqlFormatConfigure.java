package com.sxdx.kiki.server.system.configure;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import com.sxdx.kiki.common.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

/**
 * @program: kiki-cloud
 * @description: p6spy自定义打印配置类
 * @author: 高一鹏
 * @create: 2019-10-08 22:29
 **/
public class P6spySqlFormatConfigure implements MessageFormattingStrategy {


    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        return StringUtils.isNotBlank(sql) ? DateUtil.formatFullTime(LocalDateTime.now(), DateUtil.FULL_TIME_SPLIT_PATTERN)
                + " | 耗时 " + elapsed + " ms | SQL 语句：" + StringUtils.LF + sql.replaceAll("[\\s]+", StringUtils.SPACE) + ";" : StringUtils.EMPTY;
    }
}