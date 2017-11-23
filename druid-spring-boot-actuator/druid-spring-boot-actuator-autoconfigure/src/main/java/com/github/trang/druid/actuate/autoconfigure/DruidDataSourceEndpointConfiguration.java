package com.github.trang.druid.actuate.autoconfigure;

import com.github.trang.druid.actuate.DruidDataSourceMvcEndpoint;
import com.github.trang.druid.actuate.DruidDataSourceEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Druid Endpoint & Druid MvcEndpoint 自动配置，默认开启
 *
 * @author trang
 */
@Configuration
@ConditionalOnProperty(prefix = "endpoints.druid", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DruidDataSourceEndpointConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public DruidDataSourceEndpoint druidDataSourceEndpoint() {
        return new DruidDataSourceEndpoint();
    }

    @Bean
    @ConditionalOnMissingBean
    public DruidDataSourceMvcEndpoint druidDataSourceMvcEndpoint(DruidDataSourceEndpoint druidDataSourceEndpoint) {
        return new DruidDataSourceMvcEndpoint(druidDataSourceEndpoint);
    }

}