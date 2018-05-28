package com.github.trang.druid.autoconfigure;

import static com.github.trang.druid.autoconfigure.properties.DruidDataSourceProperties.DruidConstants.DRUID_STAT_VIEW_SERVLET_PREFIX;

import javax.servlet.Servlet;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.alibaba.druid.support.http.StatViewServlet;
import com.github.trang.druid.autoconfigure.properties.DruidDataSourceProperties;
import com.github.trang.druid.autoconfigure.properties.DruidDataSourceProperties.DruidStatViewServletProperties;

import lombok.extern.slf4j.Slf4j;

/**
 * Druid Servlet 配置
 *
 * @author trang
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(Servlet.class)
@ConditionalOnProperty(prefix = DRUID_STAT_VIEW_SERVLET_PREFIX, name = "enabled", havingValue = "true")
@Slf4j
public class DruidServletConfiguration {

    /**
     * Druid 提供了一个 StatViewServlet 用于展示 Druid 的统计信息
     * 这个 StatViewServlet 的用途包括：
     *   1. 提供监控信息展示的 HTML 页面
     *   2. 提供监控信息的 JSON API
     */
    @Bean
    public ServletRegistrationBean druidStatViewServlet(DruidDataSourceProperties druidProperties) {
        log.debug("druid stat-view-servlet init...");
        DruidStatViewServletProperties properties = druidProperties.getStatViewServlet();
        ServletRegistrationBean registration = new ServletRegistrationBean(new StatViewServlet());
        registration.addUrlMappings(properties.getUrlMappings());
        if (!StringUtils.isEmpty(properties.getLoginUsername())) {
            registration.addInitParameter("loginUsername", properties.getLoginUsername());
        }
        if (!StringUtils.isEmpty(properties.getLoginPassword())) {
            registration.addInitParameter("loginPassword", properties.getLoginPassword());
        }
        if (!StringUtils.isEmpty(properties.getAllow())) {
            registration.addInitParameter("allow", properties.getAllow());
        }
        if (!StringUtils.isEmpty(properties.getDeny())) {
            registration.addInitParameter("deny", properties.getDeny());
        }
        registration.addInitParameter("resetEnable", Boolean.toString(properties.isResetEnable()));
        return registration;
    }

}