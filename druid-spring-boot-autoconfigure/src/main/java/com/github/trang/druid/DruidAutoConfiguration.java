package com.github.trang.druid;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.config.ConfigFilter;
import com.alibaba.druid.filter.logging.CommonsLogFilter;
import com.alibaba.druid.filter.logging.Log4j2Filter;
import com.alibaba.druid.filter.logging.Log4jFilter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.github.trang.druid.DruidProperties.*;
import static java.util.stream.Collectors.toList;

/**
 * Druid 自动配置
 *
 * @author trang
 */
@Configuration
@ConditionalOnClass(DruidDataSource.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DruidProperties.class)
@Import({DruidStatConfiguration.class, DruidServletConfiguration.class})
public class DruidAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DruidAutoConfiguration.class);

    @ConditionalOnProperty(prefix = DRUID_STAT_FILTER_PREFIX, name = "enabled", havingValue = "true",
            matchIfMissing = true)
    @Bean
    @ConfigurationProperties(DRUID_STAT_FILTER_PREFIX)
    public StatFilter statFilter() {
        log.debug("druid stat-filter init...");
        return new StatFilter();
    }

    @ConditionalOnProperty(prefix = DRUID_WALL_FILTER_PREFIX, name = "enabled", havingValue = "true")
    @Bean
    @ConfigurationProperties(DRUID_WALL_CONFIG_PREFIX)
    public WallConfig wallConfig() {
        return new WallConfig();
    }

    @ConditionalOnProperty(prefix = DRUID_WALL_FILTER_PREFIX, name = "enabled", havingValue = "true")
    @Bean
    @ConfigurationProperties(DRUID_WALL_FILTER_PREFIX)
    public WallFilter wallFilter(WallConfig wallConfig) {
        log.debug("druid wall-filter init...");
        WallFilter filter = new WallFilter();
        filter.setConfig(wallConfig);
        return filter;
    }

    @ConditionalOnProperty(prefix = DRUID_CONFIG_FILTER_PREFIX, name = "enabled", havingValue = "true")
    @Bean
    public ConfigFilter configFilter() {
        log.debug("druid config-filter init...");
        return new ConfigFilter();
    }

    @ConditionalOnProperty(prefix = DRUID_SLF4J_FILTER_PREFIX, name = "enabled", havingValue = "true")
    @Bean
    @ConfigurationProperties(DRUID_SLF4J_FILTER_PREFIX)
    public Slf4jLogFilter slf4jLogFilter() {
        log.debug("druid slf4j-filter init...");
        return new Slf4jLogFilter();
    }

    @ConditionalOnProperty(prefix = DRUID_LOG4J_FILTER_PREFIX, name = "enabled", havingValue = "true")
    @Bean
    @ConfigurationProperties(DRUID_LOG4J_FILTER_PREFIX)
    public Log4jFilter log4jFilter() {
        log.debug("druid log4j-filter init...");
        return new Log4jFilter();
    }

    @ConditionalOnProperty(prefix = DRUID_LOG4J2_FILTER_PREFIX, name = "enabled", havingValue = "true")
    @Bean
    @ConfigurationProperties(DRUID_LOG4J2_FILTER_PREFIX)
    public Log4j2Filter log4j2Filter() {
        log.debug("druid log4j2-filter init...");
        return new Log4j2Filter();
    }

    @ConditionalOnProperty(prefix = DRUID_COMMONS_LOG_FILTER_PREFIX, name = "enabled", havingValue = "true")
    @Bean
    @ConfigurationProperties(DRUID_COMMONS_LOG_FILTER_PREFIX)
    public CommonsLogFilter commonsLogFilter() {
        log.debug("druid commons-log-filter init...");
        return new CommonsLogFilter();
    }

    /*
     * 不使用 @Value 注入，避免因为找不到值抛出 NPE
     */
    @Autowired
    private DataSourceProperties dataSourceProperties;

    @ConditionalOnMissingBean(DataSource.class)
    @Bean(initMethod = "init", destroyMethod = "close")
    @ConfigurationProperties(DRUID_DATA_SOURCE_PREFIX)
    public DruidDataSource dataSource(@Autowired(required = false) StatFilter statFilter,
                                      @Autowired(required = false) WallFilter wallFilter,
                                      @Autowired(required = false) ConfigFilter configFilter,
                                      @Autowired(required = false) Slf4jLogFilter slf4jLogFilter,
                                      @Autowired(required = false) Log4jFilter log4jFilter,
                                      @Autowired(required = false) Log4j2Filter log4j2Filter,
                                      @Autowired(required = false) CommonsLogFilter commonsLogFilter) {
        log.debug("druid data-source init...");
        DruidDataSource dataSource = new DruidDataSource();
        if (!StringUtils.isEmpty(dataSourceProperties.getDriverClassName())) {
            dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        }
        if (!StringUtils.isEmpty(dataSourceProperties.getUrl())) {
            dataSource.setUrl(dataSourceProperties.getUrl());
        }
        if (!StringUtils.isEmpty(dataSourceProperties.getUsername())) {
            dataSource.setUsername(dataSourceProperties.getUsername());
        }
        if (!StringUtils.isEmpty(dataSourceProperties.getPassword())) {
            dataSource.setPassword(dataSourceProperties.getPassword());
        }
        List<Filter> filters = Stream.of(statFilter, wallFilter, configFilter, slf4jLogFilter, log4jFilter,
                log4j2Filter, commonsLogFilter)
                .filter(Objects::nonNull)
                .collect(toList());
        dataSource.setProxyFilters(filters);
        return dataSource;
    }

}