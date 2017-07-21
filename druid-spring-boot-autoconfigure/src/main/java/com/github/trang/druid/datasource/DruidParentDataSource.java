package com.github.trang.druid.datasource;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.config.ConfigFilter;
import com.alibaba.druid.filter.logging.CommonsLogFilter;
import com.alibaba.druid.filter.logging.Log4j2Filter;
import com.alibaba.druid.filter.logging.Log4jFilter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallFilter;
import com.github.trang.druid.properties.DruidFilterProperties.DruidConfigFilterProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.github.trang.druid.properties.DruidProperties.DRUID_DATA_SOURCE_PREFIX;

/**
 * Druid 多数据源支持，会自动注入 `spring.datasource.druid` 配置
 *
 * @author trang
 */
@ConfigurationProperties(DRUID_DATA_SOURCE_PREFIX)
public abstract class DruidParentDataSource extends DruidDataSource {

    @Autowired(required = false)
    private StatFilter statFilter;
    @Autowired(required = false)
    private WallFilter wallFilter;
    @Autowired(required = false)
    private ConfigFilter configFilter;
    @Autowired(required = false)
    private Slf4jLogFilter slf4jLogFilter;
    @Autowired(required = false)
    private Log4jFilter log4jFilter;
    @Autowired(required = false)
    private Log4j2Filter log4j2Filter;
    @Autowired(required = false)
    private CommonsLogFilter commonsLogFilter;
    // 不使用 @Value 注入，避免因为找不到值抛出 NPE
    @Autowired
    private DataSourceProperties dataSourceProperties;
    @Autowired
    private DruidConfigFilterProperties druidConfigFilterProperties;

    @PostConstruct
    public void initDruidParentProperties() {
        initDataSourceProperties();
        initConfigFilterProperties();
        initFilters();
    }

    private void initFilters() {
        List<Filter> filters = super.getProxyFilters();
        Stream.of(statFilter, wallFilter, configFilter, slf4jLogFilter, log4jFilter, log4j2Filter, commonsLogFilter)
                .filter(Objects::nonNull)
                .filter(filter -> !filters.contains(filter))
                .forEach(filters::add);
    }

    private void initConfigFilterProperties() {
        if (druidConfigFilterProperties.isEnabled()) {
            StringBuilder builder = new StringBuilder();
            builder.append(ConfigFilter.CONFIG_DECRYPT).append("=").append("true").append(";");
            if (!StringUtils.isEmpty(druidConfigFilterProperties.getKey())) {
                builder.append(ConfigFilter.CONFIG_KEY).append("=").append(druidConfigFilterProperties.getKey());
                builder.append(";");
            }
            if (!StringUtils.isEmpty(druidConfigFilterProperties.getFile())) {
                builder.append(ConfigFilter.CONFIG_FILE).append("=").append(druidConfigFilterProperties.getFile());
            }
            super.setConnectionProperties(builder.toString());
        }
    }

    private void initDataSourceProperties() {
        if (!StringUtils.isEmpty(dataSourceProperties.getDriverClassName())) {
            super.setDriverClassName(dataSourceProperties.getDriverClassName());
        }
        if (!StringUtils.isEmpty(dataSourceProperties.getUrl())) {
            super.setUrl(dataSourceProperties.getUrl());
        }
        if (!StringUtils.isEmpty(dataSourceProperties.getUsername())) {
            super.setUsername(dataSourceProperties.getUsername());
        }
        if (!StringUtils.isEmpty(dataSourceProperties.getPassword())) {
            super.setPassword(dataSourceProperties.getPassword());
        }
    }

}