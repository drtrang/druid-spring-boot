package com.github.trang.druid.example.mybatis.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.trang.druid.autoconfigure.DruidDataSourceCustomizer;

import lombok.extern.slf4j.Slf4j;

/**
 * 多数据源配置，只在 #{@code spring.profiles.active=dynamic} 时生效
 *
 * @author trang
 */
@Configuration
@Profile({"dynamic", "dynamic-dev-yaml", "dynamic-dev-props"})
@Slf4j
public class SpringDataSourceConfig {

    /**
     * 定制化 DruidDataSource，所有的数据源都会生效
     *
     * @return druidDataSourceCustomizer
     */
    @Bean
    public DruidDataSourceCustomizer druidDataSourceCustomizer() {
        return druidDataSource -> log.info("DruidDataSourceCustomizer...");
    }

    /**
     * 构造 DynamicDataSource，指定数据源切换规则
     *
     * @param druidDataSourceMap druidDataSourceMap
     * @return dataSource
     */
    @Bean
    @Primary
    public DynamicDataSource dataSource(Map<String, DruidDataSource> druidDataSourceMap) {
        Map<String, DataSource> dataSourceMap = new HashMap<>(druidDataSourceMap);
        return new DynamicDataSource(dataSourceMap);
    }

}