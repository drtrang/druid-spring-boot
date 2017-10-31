package com.github.trang.druid.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.trang.druid.autoconfigure.datasource.DruidDataSource2;
import lombok.extern.slf4j.Slf4j;
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
@Profile({"dynamic", "dynamic-dev-yaml", "dynamic-dev-props"})
@Slf4j
public class SpringDataSourceConfig {

    /**
     * 第一个数据源，注意数据源类型为 #{@link DruidDataSource2}
     *
     * `spring.datasource.druid.one` 前缀的配置会注入到该 Bean，同时会继承 `spring.datasource.druid`
     * 前缀的配置，若名称相同则会被 `spring.datasource.druid.one` 覆盖
     */
    @Bean(initMethod = "init", destroyMethod = "close")
    @ConfigurationProperties("spring.datasource.druid.one")
    public DruidDataSource firstDataSource() {
        log.debug("druid first-data-source init...");
        return new DruidDataSource2();
    }

    /**
     * 第二个数据源，若还有其它数据源可以继续增加
     */
    @Bean(initMethod = "init", destroyMethod = "close")
    @ConfigurationProperties("spring.datasource.druid.two")
    public DruidDataSource secondDataSource() {
        log.debug("druid second-data-source init...");
        return new DruidDataSource2();
    }

    @Bean
    @Primary
    public DynamicDataSource dataSource(DruidDataSource firstDataSource, DruidDataSource secondDataSource) {
        Map<String, DataSource> targetDataSources = new HashMap<>(8);
        targetDataSources.put(firstDataSource.getName(), firstDataSource);
        targetDataSources.put(secondDataSource.getName(), secondDataSource);
        return new DynamicDataSource(firstDataSource, targetDataSources);
    }

}