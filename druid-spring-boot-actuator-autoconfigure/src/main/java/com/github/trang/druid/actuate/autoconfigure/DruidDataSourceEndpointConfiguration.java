package com.github.trang.druid.actuate.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author trang
 */
@Configuration
@ConditionalOnProperty(prefix = "endpoints.druid", name = "enabled", havingValue = "true")
public class DruidDataSourceEndpointConfiguration {

}