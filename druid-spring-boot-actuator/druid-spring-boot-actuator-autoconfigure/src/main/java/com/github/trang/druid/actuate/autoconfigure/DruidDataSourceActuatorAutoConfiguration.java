package com.github.trang.druid.actuate.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Druid Actuator 自动配置，默认开启
 *
 * @author trang
 */
@Configuration
@ConditionalOnClass(DruidDataSource.class)
@ConditionalOnProperty(prefix = "management.druid", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({DruidDataSourceEndpointConfiguration.class, DruidDataSourceMetadataProviderConfiguration.class})
public class DruidDataSourceActuatorAutoConfiguration {

}