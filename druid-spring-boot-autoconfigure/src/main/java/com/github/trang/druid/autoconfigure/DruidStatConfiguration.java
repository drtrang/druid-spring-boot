package com.github.trang.druid.autoconfigure;

import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import com.github.trang.druid.autoconfigure.properties.DruidDataSourceProperties;
import com.github.trang.druid.autoconfigure.properties.DruidDataSourceProperties.DruidAopStatProperties;
import com.github.trang.druid.autoconfigure.properties.DruidDataSourceProperties.DruidWebStatProperties;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.servlet.Filter;

import static com.github.trang.druid.autoconfigure.properties.DruidDataSourceProperties.DRUID_AOP_STAT_PREFIX;
import static com.github.trang.druid.autoconfigure.properties.DruidDataSourceProperties.DRUID_WEB_STAT_PREFIX;

/**
 * Druid 监控配置
 *
 * @author trang
 */
@Configuration
@Slf4j
public class DruidStatConfiguration {

    /**
     * 用于采集 Spring 和 JDBC 关联监控的数据
     */
    @Configuration
    @ConditionalOnClass(Advice.class)
    @ConditionalOnProperty(prefix = DRUID_AOP_STAT_PREFIX, name = "enabled", havingValue = "true")
    public static class DruidAopStatConfiguration {

        @Value("${spring.aop.proxy-target-class:false}")
        private boolean proxyTargetClass;

        @Bean
        public DruidStatInterceptor druidStatInterceptor() {
            return new DruidStatInterceptor();
        }

        @Bean
        public RegexpMethodPointcutAdvisor druidStatAdvisor(DruidDataSourceProperties druidDataSourceProperties,
                                                            DruidStatInterceptor druidStatInterceptor) {
            DruidAopStatProperties properties = druidDataSourceProperties.getAopStat();
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

        @Bean
        public FilterRegistrationBean filterRegistrationBean(DruidDataSourceProperties druidDataSourceProperties) {
            log.debug("druid web-stat-filter init...");
            DruidWebStatProperties properties = druidDataSourceProperties.getWebStat();
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