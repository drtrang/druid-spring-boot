package com.github.trang.druid.example.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.trang.druid.autoconfigure.DruidDataSourceCustomizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源配置，只在 #{@code spring.profiles.active=dynamic} 时生效
 *
 * @author trang
 */
@Configuration
@Profile({"dynamic", "dynamic-dev-yaml", "dynamic-dev-props"})
@Slf4j
public class SpringDataSourceConfig {

//    @Bean
    public DruidDataSourceCustomizer druidDataSourceCustomizer() {
        System.out.println("DruidDataSourceCustomizer...");
        return (dataSource) -> dataSource.setMaxActive(66);
    }

    @Bean
    @Primary
    public DynamicDataSource dataSource(Map<String, DruidDataSource> druidDataSourceMap) {
        Map<String, DataSource> dataSourceMap = new HashMap<>(druidDataSourceMap);
        return new DynamicDataSource(dataSourceMap.get("master"), dataSourceMap);
    }

}