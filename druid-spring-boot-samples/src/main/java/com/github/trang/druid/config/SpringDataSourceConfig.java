package com.github.trang.druid.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.trang.druid.datasource.DruidMultiDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
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
@Profile("dynamic")
public class SpringDataSourceConfig {

    private static final Logger log = LoggerFactory.getLogger(SpringDataSourceConfig.class);

    @Bean
    @ConfigurationProperties("spring.datasource.druid.one")
    public DruidDataSource firstDataSource() {
        log.debug("druid master-data-source init...");
        return new DruidMultiDataSource();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.druid.two")
    public DruidDataSource secondDataSource() {
        log.debug("druid slave-data-source init...");
        return new DruidMultiDataSource();
    }

    @Bean
    @Primary
    public DynamicDataSource dataSource(DruidDataSource firstDataSource, DruidDataSource secondDataSource) {
        Map<String, DataSource> targetDataSources = new HashMap<>();
        targetDataSources.put("one", firstDataSource);
        targetDataSources.put("two", secondDataSource);
        return new DynamicDataSource(firstDataSource, targetDataSources);
    }

}