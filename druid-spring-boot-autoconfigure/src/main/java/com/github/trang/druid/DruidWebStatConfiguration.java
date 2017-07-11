package com.github.trang.druid;

import com.alibaba.druid.support.http.WebStatFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * 用于采集 Web 和 JDBC 关联监控的数据
 *
 * @author trang
 */
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "spring.datasource.druid.web.web-stat-filter", name = "enabled", havingValue = "true")
public class DruidWebStatConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DruidAutoConfiguration.class);

    @Bean
    public FilterRegistrationBean filterRegistrationBean(DruidStatProperties properties) {
        log.debug("------ 初始化 Druid WebStatFilter ------");
        FilterRegistrationBean registration = new FilterRegistrationBean();
        DruidStatProperties.WebStatFilter config = properties.getWebStatFilter();
        WebStatFilter filter = new WebStatFilter();
        registration.setFilter(filter);
        registration.addUrlPatterns(config.getUrlPattern());
        registration.addInitParameter("exclusions", config.getExclusions());
        registration.addInitParameter("sessionStatEnable", Boolean.toString(config.getSessionStatEnable()));
        registration.addInitParameter("profileEnable", Boolean.toString(config.getProfileEnable()));
        if (config.getSessionStatMaxCount() != null) {
            registration.addInitParameter("sessionStatMaxCount", config.getSessionStatMaxCount());
        }
        if (config.getPrincipalSessionName() != null) {
            registration.addInitParameter("principalSessionName", config.getPrincipalSessionName());
        }
        if (config.getPrincipalCookieName() != null) {
            registration.addInitParameter("principalCookieName", config.getPrincipalCookieName());
        }
        return registration;
    }

}