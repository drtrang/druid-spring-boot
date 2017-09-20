package com.github.trang.druid;

import com.alibaba.druid.support.http.StatViewServlet;
import com.github.trang.druid.properties.DruidServletProperties.DruidStatViewServletProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import static com.github.trang.druid.properties.DruidServletProperties.DRUID_STAT_VIEW_SERVLET_PREFIX;

/**
 * Druid Servlet 配置
 *
 * @author trang
 */
@ConditionalOnWebApplication
@ConditionalOnProperty(prefix = DRUID_STAT_VIEW_SERVLET_PREFIX, name = "enabled", havingValue = "true")
@EnableConfigurationProperties(DruidStatViewServletProperties.class)
public class DruidServletConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DruidServletConfiguration.class);

    /**
     * Druid 提供了一个 StatViewServlet 用于展示 Druid 的统计信息
     * 这个 StatViewServlet 的用途包括：
     *   1. 提供监控信息展示的 HTML 页面
     *   2. 提供监控信息的 JSON API
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean(DruidStatViewServletProperties properties) {
        log.debug("druid stat-view-servlet init...");
        ServletRegistrationBean registration = new ServletRegistrationBean();
        StatViewServlet statViewServlet = new StatViewServlet();
        registration.setServlet(statViewServlet);
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