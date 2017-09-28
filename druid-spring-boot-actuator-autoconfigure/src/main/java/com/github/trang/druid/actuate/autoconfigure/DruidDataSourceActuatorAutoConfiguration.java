package com.github.trang.druid.actuate.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author trang
 */
@Configuration
@ConditionalOnClass(DruidDataSource.class)
@ConditionalOnProperty(prefix = "management.druid", name = "enabled", matchIfMissing = true)
//@Import({DruidDataSourceEndpointConfiguration.class, DruidDataSourceMetadataProviderConfiguration.class})
public class DruidDataSourceActuatorAutoConfiguration {

}