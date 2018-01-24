package com.github.trang.druid.example.mybatis.config;

import lombok.Getter;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 动态数据源，基于简单的轮询算法
 *
 * @author trang
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    private AtomicInteger count = new AtomicInteger();
    @Getter private Map<String, DataSource> targetDataSources;

    public DynamicDataSource(Map<String, DataSource> targetDataSources) {
        this.targetDataSources = targetDataSources;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        int i = count.incrementAndGet();
        List<String> list = new ArrayList<>(targetDataSources.keySet());
        return list.get(i % list.size());
    }

    @Override
    public void afterPropertiesSet() {
        super.setTargetDataSources(new HashMap<>(targetDataSources));
        super.afterPropertiesSet();
    }

}