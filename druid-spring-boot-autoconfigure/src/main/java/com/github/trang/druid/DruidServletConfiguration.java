package com.github.trang.druid;

import com.alibaba.druid.support.http.StatViewServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import static com.github.trang.druid.DruidProperties.DRUID_STAT_VIEW_SERVLET_PREFIX;

/**
 * Druid Servlet 配置
 *
 * @author trang
 */
@ConditionalOnWebApplication
public class DruidServletConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DruidServletConfiguration.class);

    /**
     * Druid 提供了一个 StatViewServlet 用于展示 Druid 的统计信息
     * 这个 StatViewServlet 的用途包括：
     *  1. 提供监控信息展示的 HTML 页面
     *  2. 提供监控信息的 JSON API
     */
    @ConditionalOnProperty(prefix = DRUID_STAT_VIEW_SERVLET_PREFIX, name = "enabled", havingValue = "true")
    @Bean
    public ServletRegistrationBean servletRegistrationBean(DruidProperties properties) {
        log.debug("druid stat-view-servlet init...");
        ServletRegistrationBean registration = new ServletRegistrationBean();
        DruidProperties.StatViewServlet config = properties.getStatViewServlet();
        StatViewServlet statViewServlet = new StatViewServlet();
        registration.setServlet(statViewServlet);
        registration.addUrlMappings(config.getUrlPattern());
        if (!StringUtils.isEmpty(config.getLoginUsername())) {
            registration.addInitParameter("loginUsername", config.getLoginUsername());
        }
        if (!StringUtils.isEmpty(config.getLoginPassword())) {
            registration.addInitParameter("loginPassword", config.getLoginPassword());
        }
        if (!StringUtils.isEmpty(config.getAllow())) {
            registration.addInitParameter("allow", config.getAllow());
        }
        if (!StringUtils.isEmpty(config.getDeny())) {
            registration.addInitParameter("deny", config.getDeny());
        }
        registration.addInitParameter("resetEnable", Boolean.toString(config.isResetEnable()));
        return registration;
    }

}