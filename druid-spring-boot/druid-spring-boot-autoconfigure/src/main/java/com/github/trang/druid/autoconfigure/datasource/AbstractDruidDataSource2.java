package com.github.trang.druid.autoconfigure.datasource;

import static com.github.trang.druid.autoconfigure.properties.DruidConstants.DRUID_DATA_SOURCE_PREFIX;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.FilterAdapter;
import com.alibaba.druid.filter.config.ConfigFilter;
import com.alibaba.druid.filter.encoding.CharsetParameter;
import com.alibaba.druid.pool.DruidDataSource;
import com.github.trang.druid.autoconfigure.properties.DruidDataSourceProperties;
import com.github.trang.druid.autoconfigure.properties.DruidDataSourceProperties.DruidConfigFilterProperties;
import com.github.trang.druid.autoconfigure.properties.DruidDataSourceProperties.DruidEncodingFilterProperties;

/**
 * Druid 多数据源支持，会自动注入 'spring.datasource' 和 'spring.datasource.druid' 配置
 *
 * @author trang
 */
@ConfigurationProperties(prefix = DRUID_DATA_SOURCE_PREFIX)
public abstract class AbstractDruidDataSource2 extends DruidDataSource {

    private static final long serialVersionUID = -5155027912840793367L;

    @Autowired
    private DataSourceProperties dataSourceProperties;
    @Autowired
    private DruidDataSourceProperties druidProperties;
    @Autowired
    private ObjectProvider<List<FilterAdapter>> druidFilters;

    @PostConstruct
    public void initDruidParentProperties() {
        initDataSourceProperties();
        initConnectionProperties();
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

    private void initConnectionProperties() {
        StringBuilder builder = new StringBuilder();
        // 解析 config-filter 的配置
        DruidConfigFilterProperties configProperties = druidProperties.getConfig();
        if (configProperties.isEnabled()) {
            builder.append(ConfigFilter.CONFIG_DECRYPT).append("=").append("true").append(";");
            if (!StringUtils.isEmpty(configProperties.getKey())) {
                builder.append(ConfigFilter.CONFIG_KEY).append("=").append(configProperties.getKey()).append(";");
            }
            if (!StringUtils.isEmpty(configProperties.getFile())) {
                builder.append(ConfigFilter.CONFIG_FILE).append("=").append(configProperties.getFile()).append(";");
            }
        }
        // 解析 encoding-filter 的配置
        DruidEncodingFilterProperties encodingProperties = druidProperties.getEncoding();
        if (encodingProperties.isEnabled()) {
            if (!StringUtils.isEmpty(encodingProperties.getClientEncoding())) {
                builder.append(CharsetParameter.CLIENTENCODINGKEY).append("=").append(encodingProperties.getClientEncoding()).append(";");
            }
            if (!StringUtils.isEmpty(encodingProperties.getServerEncoding())) {
                builder.append(CharsetParameter.SERVERENCODINGKEY).append("=").append(encodingProperties.getServerEncoding()).append(";");
            }
        }
        if (builder.length() > 0) {
            super.setConnectionProperties(builder.toString());
        }
    }

    private void initFilters() {
        List<Filter> proxyFilters = super.getProxyFilters();
        druidFilters.getIfAvailable(ArrayList::new).stream()
                .filter(Objects::nonNull)
                .filter(filter -> !proxyFilters.contains(filter))
                .forEach(proxyFilters::add);
    }

}