package com.github.trang.druid.actuate.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.trang.druid.actuate.DruidDataSourcePoolMetadata;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author trang
 */
@Configuration
@ConditionalOnClass(DruidDataSource.class)
@ConditionalOnBean(DruidDataSource.class)
public class DruidDataSourceMetadataProviderConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DataSourcePoolMetadataProvider dataSourcePoolMetadataProvider() {
        return this::getDataSourcePoolMetadata;
    }

    private DataSourcePoolMetadata getDataSourcePoolMetadata(DataSource dataSource) {
        return dataSource instanceof DruidDataSource ?
                new DruidDataSourcePoolMetadata((DruidDataSource) dataSource) : null;
    }

}