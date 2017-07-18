package com.github.trang.druid;

import com.github.trang.druid.datasource.init.DataSourceInitializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * 支持数据源初始化时执行 SQL
 *
 * 照搬自 #{@link org.springframework.boot.autoconfigure.jdbc.DataSourceInitializer}
 *
 * 在多数据源场景下，#{@link DataSourceAutoConfiguration} 会报 #{@code DataSource} 和
 * #{@code DataSourceInitializer} 的循环依赖，目前的解决办法是排除 #{@link DataSourceAutoConfiguration}，
 * 但是这样一来，Spring Boot 在数据源初始化时执行 SQL 的特性会被移除，此类是为了保留该特性
 *
 * @author trang
 */
public class DataSourceInitializerConfiguration {

    @Bean
    @ConditionalOnMissingBean(DataSourceInitializer.class)
    public com.github.trang.druid.datasource.init.DataSourceInitializer customDataSourceInitializer
            (DataSourceProperties properties, ApplicationContext applicationContext) {
        return new DataSourceInitializer(properties, applicationContext);
    }

}
