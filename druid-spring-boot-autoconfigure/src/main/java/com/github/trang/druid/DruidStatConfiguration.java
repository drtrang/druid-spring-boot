package com.github.trang.druid;

import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import com.github.trang.druid.properties.DruidStatProperties.DruidAopStatProperties;
import com.github.trang.druid.properties.DruidStatProperties.DruidWebStatProperties;
import org.aopalliance.aop.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;

import static com.github.trang.druid.properties.DruidStatProperties.DRUID_AOP_STAT_PREFIX;
import static com.github.trang.druid.properties.DruidStatProperties.DRUID_WEB_STAT_PREFIX;

/**
 * Druid 监控配置
 *
 * @author trang
 */
@EnableConfigurationProperties({DruidWebStatProperties.class, DruidAopStatProperties.class})
public class DruidStatConfiguration {

    /**
     * 用于采集 Spring 和 JDBC 关联监控的数据
     */
    @Configuration
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
        public RegexpMethodPointcutAdvisor druidStatAdvisor(DruidAopStatProperties properties,
                                                            DruidStatInterceptor druidStatInterceptor) {
            return new RegexpMethodPointcutAdvisor(properties.getPatterns(), druidStatInterceptor);
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
    @Configuration
    @ConditionalOnWebApplication
    @ConditionalOnClass(Filter.class)
    @ConditionalOnProperty(prefix = DRUID_WEB_STAT_PREFIX, name = "enabled", havingValue = "true")
    public static class DruidWebStatConfiguration {

        private static final Logger log = LoggerFactory.getLogger(DruidStatConfiguration.class);

        @Bean
        public FilterRegistrationBean filterRegistrationBean(DruidWebStatProperties properties) {
            log.debug("druid web-stat-filter init...");
            FilterRegistrationBean registration = new FilterRegistrationBean();
            WebStatFilter filter = new WebStatFilter();
            registration.setFilter(filter);
            registration.addUrlPatterns(properties.getUrlPatterns());
            registration.addInitParameter("exclusions", properties.getExclusions());
            registration.addInitParameter("sessionStatEnable", Boolean.toString(properties.isSessionStatEnable()));
            if (!StringUtils.isEmpty(properties.getSessionStatMaxCount())) {
                registration.addInitParameter("sessionStatMaxCount",Integer.toString(properties.getSessionStatMaxCount()));
            }
            if (!StringUtils.isEmpty(properties.getPrincipalSessionName())) {
                registration.addInitParameter("principalSessionName", properties.getPrincipalSessionName());
            }
            if (!StringUtils.isEmpty(properties.getPrincipalCookieName())) {
                registration.addInitParameter("principalCookieName", properties.getPrincipalCookieName());
            }
            registration.addInitParameter("profileEnable", Boolean.toString(properties.isProfileEnable()));
            return registration;
        }

    }

}