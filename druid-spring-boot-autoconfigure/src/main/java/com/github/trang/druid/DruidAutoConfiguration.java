package com.github.trang.druid;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.config.ConfigFilter;
import com.alibaba.druid.filter.logging.CommonsLogFilter;
import com.alibaba.druid.filter.logging.Log4j2Filter;
import com.alibaba.druid.filter.logging.Log4jFilter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Druid 自动配置
 *
 * @author trang
 */
@Configuration
@ConditionalOnClass(DruidDataSource.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DruidStatProperties.class)
@Import({DruidAopStatConfiguration.class, DruidWebStatConfiguration.class, DruidStatViewServletConfiguration.class})
public class DruidAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DruidAutoConfiguration.class);

    private static final String DRUID_DATA_SOURCE_PREFIX = "spring.datasource.druid";
    private static final String DRUID_WALL_FILTER_PROPERTIES_PREFIX = "spring.datasource.druid.wall";
    private static final String DRUID_STAT_FILTER_PROPERTIES_PREFIX = "spring.datasource.druid.stat";
    private static final String DRUID_CONFIG_FILTER_PROPERTIES_PREFIX = "spring.datasource.druid.config";
    private static final String DRUID_SLF4J_FILTER_PROPERTIES_PREFIX = "spring.datasource.druid.slf4j";
    private static final String DRUID_LOG4J_FILTER_PROPERTIES_PREFIX = "spring.datasource.druid.log4j";
    private static final String DRUID_LOG4J2_FILTER_PROPERTIES_PREFIX = "spring.datasource.druid.log4j2";
    private static final String DRUID_COMMONS_LOGGING_FILTER_PROPERTIES_PREFIX = "spring.datasource.druid.commonlogging";

    @ConditionalOnProperty(prefix = DRUID_STAT_FILTER_PROPERTIES_PREFIX, name = "enabled",
            havingValue = "true", matchIfMissing = true)
    @Bean
    @ConfigurationProperties(DRUID_STAT_FILTER_PROPERTIES_PREFIX)
    public StatFilter statFilter() {
        log.debug("------ 初始化 Druid 状态监控 ------");
        return new StatFilter();
    }

    @ConditionalOnProperty(prefix = DRUID_WALL_FILTER_PROPERTIES_PREFIX, name = "enabled",
            havingValue = "true")
    @Bean
    @ConfigurationProperties(DRUID_WALL_FILTER_PROPERTIES_PREFIX)
    public WallFilter wallFilter() {
        log.debug("------ 初始化 Druid 防火墙 ------");
        return new WallFilter();
    }

    @ConditionalOnProperty(prefix = DRUID_CONFIG_FILTER_PROPERTIES_PREFIX, name = "enabled",
            havingValue = "true")
    @Bean
    @ConfigurationProperties(DRUID_CONFIG_FILTER_PROPERTIES_PREFIX)
    public ConfigFilter configFilter() {
        log.debug("------ 初始化 Druid ConfigFilter ------");
        return new ConfigFilter();
    }

    @ConditionalOnProperty(prefix = DRUID_SLF4J_FILTER_PROPERTIES_PREFIX, name = "enabled",
            havingValue = "true")
    @Bean
    @ConfigurationProperties(DRUID_SLF4J_FILTER_PROPERTIES_PREFIX)
    public Slf4jLogFilter slf4jLogFilter() {
        log.debug("------ 初始化 Druid Slf4j 日志输出 ------");
        return new Slf4jLogFilter();
    }

    @ConditionalOnProperty(prefix = DRUID_LOG4J_FILTER_PROPERTIES_PREFIX, name = "enabled",
            havingValue = "true")
    @Bean
    @ConfigurationProperties(DRUID_LOG4J_FILTER_PROPERTIES_PREFIX)
    public Log4jFilter log4jFilter() {
        log.debug("------ 初始化 Druid Log4j 日志输出 ------");
        return new Log4jFilter();
    }

    @ConditionalOnProperty(prefix = DRUID_LOG4J2_FILTER_PROPERTIES_PREFIX, name = "enabled",
            havingValue = "true")
    @Bean
    @ConfigurationProperties(DRUID_LOG4J2_FILTER_PROPERTIES_PREFIX)
    public Log4j2Filter log4j2Filter() {
        log.debug("------ 初始化 Druid Log4j2 日志输出 ------");
        return new Log4j2Filter();
    }

    @ConditionalOnProperty(prefix = DRUID_COMMONS_LOGGING_FILTER_PROPERTIES_PREFIX, name = "enabled",
            havingValue = "true")
    @Bean
    @ConfigurationProperties(DRUID_COMMONS_LOGGING_FILTER_PROPERTIES_PREFIX)
    public CommonsLogFilter commonsLogFilter() {
        log.debug("------ 初始化 Druid CommonsLog 日志输出 ------");
        return new CommonsLogFilter();
    }

    @Value("${spring.datasource.druid.driver-class-name:${spring.datasource.driver-class-name:com.mysql.jdbc.Driver}}")
    private String driverClassName;
    @Value("${spring.datasource.druid.url:${spring.datasource.url}}")
    private String url;
    @Value("${spring.datasource.druid.username:${spring.datasource.username}}")
    private String username;
    @Value("${spring.datasource.druid.password:${spring.datasource.password}}")
    private String password;

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
        log.debug("------ 初始化 Druid 数据源 ------");
        DruidDataSource dataSource = new DruidDataSource();
        if (driverClassName != null && !driverClassName.isEmpty()) {
            dataSource.setDriverClassName(driverClassName);
        }
        if (url != null && !url.isEmpty()) {
            dataSource.setUrl(url);
        }
        if (username != null && !username.isEmpty()) {
            dataSource.setUsername(username);
        }
        if (password != null && !password.isEmpty()) {
            dataSource.setPassword(password);
        }
        List<Filter> filters = Stream.of(statFilter, wallFilter, configFilter, slf4jLogFilter, log4jFilter,
                log4j2Filter, commonsLogFilter)
                .filter(Objects::nonNull)
                .collect(toList());
        dataSource.setProxyFilters(filters);
        return dataSource;
    }

}