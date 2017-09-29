package com.github.trang.druid.autoconfigure.datasource;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.FilterAdapter;
import com.alibaba.druid.filter.config.ConfigFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.github.trang.druid.autoconfigure.properties.DruidDataSourceProperties;
import com.github.trang.druid.autoconfigure.properties.DruidDataSourceProperties.DruidConfigFilterProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.github.trang.druid.autoconfigure.properties.DruidDataSourceProperties.DRUID_DATA_SOURCE_PREFIX;

/**
 * Druid 多数据源支持，会自动注入 `spring.datasource.druid` 配置
 *
 * @author trang
 */
@ConfigurationProperties(DRUID_DATA_SOURCE_PREFIX)
public abstract class DruidDataSource2Support extends DruidDataSource {

    @Autowired
    private DataSourceProperties dataSourceProperties;
    @Autowired
    private DruidDataSourceProperties druidDataSourceProperties;
    @Autowired
    private Optional<List<FilterAdapter>> druidFilters;

    @PostConstruct
    public void initDruidParentProperties() {
        initDataSourceProperties();
        initConfigFilterProperties();
        initFilters();
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

    private void initConfigFilterProperties() {
        DruidConfigFilterProperties configFilterProperties = druidDataSourceProperties.getConfig();
        if (configFilterProperties.isEnabled()) {
            StringBuilder builder = new StringBuilder();
            builder.append(ConfigFilter.CONFIG_DECRYPT).append("=").append("true").append(";");
            if (!StringUtils.isEmpty(configFilterProperties.getKey())) {
                builder.append(ConfigFilter.CONFIG_KEY).append("=").append(configFilterProperties.getKey());
                builder.append(";");
            }
            if (!StringUtils.isEmpty(configFilterProperties.getFile())) {
                builder.append(ConfigFilter.CONFIG_FILE).append("=").append(configFilterProperties.getFile());
            }
            super.setConnectionProperties(builder.toString());
        }
    }

    private void initFilters() {
        List<Filter> filters = super.getProxyFilters();
        druidFilters.ifPresent(dfs -> dfs.stream()
                .filter(Objects::nonNull)
                .filter(filter -> !filters.contains(filter))
                .forEach(filters::add));
    }

}