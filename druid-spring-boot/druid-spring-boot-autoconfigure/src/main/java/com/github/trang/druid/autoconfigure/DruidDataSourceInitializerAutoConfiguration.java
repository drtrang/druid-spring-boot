package com.github.trang.druid.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.trang.druid.autoconfigure.datasource.init.DataSourceInitializerInvoker;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 支持数据源初始化时执行 SQL
 *
 * 在多数据源场景下，#{@link DataSourceAutoConfiguration} 会报 #{@code DataSource} 和
 * #{@code DruidDataSourceInitializer} 的循环依赖，目前的解决办法是排除 #{@link DataSourceAutoConfiguration}，
 * 但是这样一来，Spring Boot 在数据源初始化时执行 SQL 的特性会被移除，此类是为了保留该特性
 *
 * 同时增加了可以在多个数据源执行 SQL 的功能
 *
 * @author trang
 */
@Configuration
@Import(DataSourceInitializerInvoker.class)
@ConditionalOnBean(DruidDataSource.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class DruidDataSourceInitializerAutoConfiguration {

}