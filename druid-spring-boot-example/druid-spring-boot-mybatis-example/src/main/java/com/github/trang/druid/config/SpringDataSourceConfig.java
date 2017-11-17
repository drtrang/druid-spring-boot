package com.github.trang.druid.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Map.Entry;

import static java.util.stream.Collectors.toMap;

/**
 * 多数据源配置，只在 #{@code spring.profiles.active=dynamic} 时生效
 *
 * @author trang
 */
@Configuration
@Profile({"dynamic", "dynamic-dev-yaml", "dynamic-dev-props"})
@Slf4j
public class SpringDataSourceConfig {

    @Autowired
    private Map<String, DruidDataSource> druidDataSourceMap;

    @Bean
    @Primary
    public DynamicDataSource dataSource() {
        Map<String, DataSource> dataSourceMap = druidDataSourceMap.entrySet().stream()
                .collect(toMap(Entry::getKey, Entry::getValue));
        return new DynamicDataSource(dataSourceMap.get("master"), dataSourceMap);
    }

}