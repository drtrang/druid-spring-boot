package com.github.trang.druid.actuate.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.trang.druid.actuate.DruidDataSourcePoolMetadata;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Druid Metadata 自动配置，适用于 Metrics，默认开启
 *
 * @author trang
 */
@Configuration
@ConditionalOnClass(DruidDataSource.class)
public class DruidDataSourceMetadataProviderConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DataSourcePoolMetadataProvider dataSourcePoolMetadataProvider() {
        return (dataSource) -> {
            if (dataSource instanceof DruidDataSource) {
                return new DruidDataSourcePoolMetadata((DruidDataSource) dataSource);
            }
            return null;
        };
    }

}