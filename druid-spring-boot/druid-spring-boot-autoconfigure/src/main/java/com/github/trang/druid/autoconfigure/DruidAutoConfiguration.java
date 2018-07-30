package com.github.trang.druid.autoconfigure;

import static com.github.trang.druid.autoconfigure.properties.DruidConstants.DRUID_COMMONS_LOG_FILTER_PREFIX;
import static com.github.trang.druid.autoconfigure.properties.DruidConstants.DRUID_CONFIG_FILTER_PREFIX;
import static com.github.trang.druid.autoconfigure.properties.DruidConstants.DRUID_ENCODING_FILTER_PREFIX;
import static com.github.trang.druid.autoconfigure.properties.DruidConstants.DRUID_LOG4J2_FILTER_PREFIX;
import static com.github.trang.druid.autoconfigure.properties.DruidConstants.DRUID_LOG4J_FILTER_PREFIX;
import static com.github.trang.druid.autoconfigure.properties.DruidConstants.DRUID_SLF4J_FILTER_PREFIX;
import static com.github.trang.druid.autoconfigure.properties.DruidConstants.DRUID_STAT_FILTER_PREFIX;
import static com.github.trang.druid.autoconfigure.properties.DruidConstants.DRUID_WALL_CONFIG_PREFIX;
import static com.github.trang.druid.autoconfigure.properties.DruidConstants.DRUID_WALL_FILTER_PREFIX;

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

import com.alibaba.druid.filter.config.ConfigFilter;
import com.alibaba.druid.filter.encoding.EncodingConvertFilter;
import com.alibaba.druid.filter.logging.CommonsLogFilter;
import com.alibaba.druid.filter.logging.Log4j2Filter;
import com.alibaba.druid.filter.logging.Log4jFilter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.github.trang.druid.autoconfigure.properties.DruidDataSourceProperties;

import lombok.extern.slf4j.Slf4j;

/**
 * Druid 连接池的自动配置
 *
 * @author trang
 */
@Configuration
@ConditionalOnClass(DruidDataSource.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties({DataSourceProperties.class, DruidDataSourceProperties.class})
@Import({DruidDataSourceConfiguration.class, DruidServletConfiguration.class, DruidStatConfiguration.class})
@Slf4j
public class DruidAutoConfiguration {

    /**
     * https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_StatFilter
     *
     * @return statFilter
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = DRUID_STAT_FILTER_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
    @ConfigurationProperties(prefix = DRUID_STAT_FILTER_PREFIX)
    public StatFilter statFilter() {
        log.debug("druid stat-filter init...");
        return new StatFilter();
    }

    /**
     * https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE-wallfilter
     *
     * @return wallConfig
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = DRUID_WALL_FILTER_PREFIX, name = "enabled", havingValue = "true")
    @ConfigurationProperties(prefix = DRUID_WALL_CONFIG_PREFIX)
    public WallConfig wallConfig() {
        return new WallConfig();
    }

    /**
     * https://github.com/alibaba/druid/wiki/%E7%AE%80%E4%BB%8B_WallFilter
     * https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE-wallfilter
     *
     * @return wallFilter
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = DRUID_WALL_FILTER_PREFIX, name = "enabled", havingValue = "true")
    @ConfigurationProperties(prefix = DRUID_WALL_FILTER_PREFIX)
    public WallFilter wallFilter(WallConfig wallConfig) {
        log.debug("druid wall-filter init...");
        WallFilter filter = new WallFilter();
        filter.setConfig(wallConfig);
        return filter;
    }

    /**
     * https://github.com/alibaba/druid/wiki/%E4%BD%BF%E7%94%A8ConfigFilter
     *
     * @return configFilter
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = DRUID_CONFIG_FILTER_PREFIX, name = "enabled", havingValue = "true")
    public ConfigFilter configFilter() {
        log.debug("druid config-filter init...");
        return new ConfigFilter();
    }

    /**
     * https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_LogFilter
     *
     * @return slf4jLogFilter
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = DRUID_SLF4J_FILTER_PREFIX, name = "enabled", havingValue = "true")
    @ConfigurationProperties(prefix = DRUID_SLF4J_FILTER_PREFIX)
    public Slf4jLogFilter slf4jLogFilter() {
        log.debug("druid slf4j-filter init...");
        return new Slf4jLogFilter();
    }

    /**
     * https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_LogFilter
     *
     * @return log4jFilter
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = DRUID_LOG4J_FILTER_PREFIX, name = "enabled", havingValue = "true")
    @ConfigurationProperties(prefix = DRUID_LOG4J_FILTER_PREFIX)
    public Log4jFilter log4jFilter() {
        log.debug("druid log4j-filter init...");
        return new Log4jFilter();
    }

    /**
     * https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_LogFilter
     *
     * @return log4j2Filter
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = DRUID_LOG4J2_FILTER_PREFIX, name = "enabled", havingValue = "true")
    @ConfigurationProperties(prefix = DRUID_LOG4J2_FILTER_PREFIX)
    public Log4j2Filter log4j2Filter() {
        log.debug("druid log4j2-filter init...");
        return new Log4j2Filter();
    }

    /**
     * https://github.com/alibaba/druid/wiki/%E9%85%8D%E7%BD%AE_LogFilter
     *
     * @return commonsLogFilter
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = DRUID_COMMONS_LOG_FILTER_PREFIX, name = "enabled", havingValue = "true")
    @ConfigurationProperties(prefix = DRUID_COMMONS_LOG_FILTER_PREFIX)
    public CommonsLogFilter commonsLogFilter() {
        log.debug("druid commons-log-filter init...");
        return new CommonsLogFilter();
    }

    /**
     * https://github.com/alibaba/druid/wiki/%E4%BD%BF%E7%94%A8EncodingConvertFilter
     * <p>
     * 需要配合 connectionProperties 使用，clientEncoding=UTF-8;serverEncoding=ISO-8859-1
     *
     * @return encodingConvertFilter
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = DRUID_ENCODING_FILTER_PREFIX, name = "enabled", havingValue = "true")
    @ConfigurationProperties(prefix = DRUID_ENCODING_FILTER_PREFIX)
    public EncodingConvertFilter encodingConvertFilter() {
        log.debug("druid encoding-convert-filter init...");
        return new EncodingConvertFilter();
    }

}