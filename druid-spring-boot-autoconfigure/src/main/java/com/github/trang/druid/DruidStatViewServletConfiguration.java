package com.github.trang.druid;

import com.alibaba.druid.support.http.StatViewServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

/**
 * Druid 提供了一个 StatViewServlet 用于展示 Druid 的统计信息
 * 这个 StatViewServlet 的用途包括：
 *  1. 提供监控信息展示的 HTML 页面
 *  2. 提供监控信息的 JSON API
 *
 * @author trang
 */
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = "spring.datasource.druid.web.stat-view-servlet", name = "enabled", havingValue = "true")
public class DruidStatViewServletConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DruidAutoConfiguration.class);

    @Bean
    public ServletRegistrationBean servletRegistrationBean(DruidStatProperties properties) {
        log.debug("------ 初始化 Druid StatViewServlet ------");
        ServletRegistrationBean registration = new ServletRegistrationBean();
        DruidStatProperties.StatViewServlet config = properties.getStatViewServlet();
        registration.setServlet(new StatViewServlet());
        registration.addUrlMappings(config.getUrlPattern());
        registration.addInitParameter("resetEnable", Boolean.toString(config.getResetEnable()));
        if (config.getAllow() != null) {
            registration.addInitParameter("allow", config.getAllow());
        }
        if (config.getDeny() != null) {
            registration.addInitParameter("deny", config.getDeny());
        }
        if (config.getLoginUsername() != null) {
            registration.addInitParameter("loginUsername", config.getLoginUsername());
        }
        if (config.getLoginPassword() != null) {
            registration.addInitParameter("loginPassword", config.getLoginPassword());
        }
        return registration;
    }

}