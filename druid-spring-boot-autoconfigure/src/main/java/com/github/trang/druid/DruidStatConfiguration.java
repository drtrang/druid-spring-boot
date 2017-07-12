package com.github.trang.druid;

import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import com.github.trang.druid.DruidProperties.WebStat;
import org.aopalliance.aop.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import static com.github.trang.druid.DruidProperties.DRUID_AOP_STAT_PREFIX;
import static com.github.trang.druid.DruidProperties.DRUID_WEB_STATE_PREFIX;

/**
 * Druid 监控配置
 *
 * @author trang
 */
public class DruidStatConfiguration {

    /**
     * 用于采集 Spring 和 JDBC 关联监控的数据
     */
    @ConditionalOnClass(Advice.class)
    @ConditionalOnProperty(prefix = DRUID_AOP_STAT_PREFIX, name = "enabled", havingValue = "true")
    public static class DruidAopStatConfiguration {

        private static final Logger log = LoggerFactory.getLogger(DruidStatConfiguration.class);

        @Value("${spring.aop.proxy-target-class:false}")
        private boolean proxyTargetClass;

        @Bean
        public DruidStatInterceptor druidStatInterceptor() {
            return new DruidStatInterceptor();
        }

        @Bean
        public RegexpMethodPointcutAdvisor druidStatAdvisor(DruidProperties druidProperties,
                                                            DruidStatInterceptor druidStatInterceptor) {
            return new RegexpMethodPointcutAdvisor(druidProperties.getAopStat().getPatterns(), druidStatInterceptor);
        }

        @Bean
        public DefaultAdvisorAutoProxyCreator druidStatProxyCreator() {
            log.debug("druid aop-stat init...");
            DefaultAdvisorAutoProxyCreator druidStatProxyCreator = new DefaultAdvisorAutoProxyCreator();
            druidStatProxyCreator.setProxyTargetClass(proxyTargetClass);
            return druidStatProxyCreator;
        }

    }

    /**
     * 用于采集 Web 和 JDBC 关联监控的数据
     */
    @ConditionalOnWebApplication
    public static class DruidWebStatConfiguration {

        private static final Logger log = LoggerFactory.getLogger(DruidStatConfiguration.class);

        @ConditionalOnProperty(prefix = DRUID_WEB_STATE_PREFIX, name = "enabled", havingValue = "true")
        @Bean
        public FilterRegistrationBean filterRegistrationBean(DruidProperties properties) {
            log.debug("druid web-stat-filter init...");
            FilterRegistrationBean registration = new FilterRegistrationBean();
            WebStat config = properties.getWebStat();
            WebStatFilter filter = new WebStatFilter();
            registration.setFilter(filter);
            registration.addUrlPatterns(config.getUrlPatterns());
            registration.addInitParameter("exclusions", config.getExclusions());
            registration.addInitParameter("sessionStatEnable", Boolean.toString(config.isSessionStatEnable()));
            if (!StringUtils.isEmpty(config.getSessionStatMaxCount())) {
                registration.addInitParameter("sessionStatMaxCount",Integer.toString(config.getSessionStatMaxCount()));
            }
            if (!StringUtils.isEmpty(config.getPrincipalSessionName())) {
                registration.addInitParameter("principalSessionName", config.getPrincipalSessionName());
            }
            if (!StringUtils.isEmpty(config.getPrincipalCookieName())) {
                registration.addInitParameter("principalCookieName", config.getPrincipalCookieName());
            }
            registration.addInitParameter("profileEnable", Boolean.toString(config.isProfileEnable()));
            return registration;
        }

    }

}