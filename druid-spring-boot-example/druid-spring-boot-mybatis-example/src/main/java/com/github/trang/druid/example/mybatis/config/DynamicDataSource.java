package com.github.trang.druid.example.mybatis.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 动态数据源，基于简单的轮询算法
 *
 * @author trang
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public class DynamicDataSource extends AbstractRoutingDataSource {

    private final AtomicInteger count = new AtomicInteger();
    private final Map<String, DataSource> targetDataSources;

    /**
     * 基于轮询算法指定实际的数据源
     *
     * @return dataSource
     */
    @Override
    protected Object determineCurrentLookupKey() {
        int i = count.incrementAndGet();
        List<String> list = new ArrayList<>(targetDataSources.keySet());
        String dataSource = list.get(i % list.size());
        log.info(">>>>>> 当前数据库: {} <<<<<<", dataSource);
        return dataSource;
    }

    @Override
    public void afterPropertiesSet() {
        super.setTargetDataSources(new HashMap<>(targetDataSources));
        super.afterPropertiesSet();
    }

}