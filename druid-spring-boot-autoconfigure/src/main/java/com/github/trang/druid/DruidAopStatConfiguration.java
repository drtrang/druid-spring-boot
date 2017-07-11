package com.github.trang.druid;

import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import com.github.trang.druid.DruidAopStatConfiguration.DruidAopProperties;
import org.aopalliance.aop.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 用于采集 Spring 和 JDBC 关联监控的数据
 *
 * @author trang
 */
@ConditionalOnClass(Advice.class)
@ConditionalOnProperty(prefix = "spring.datasource.druid.aop", name = "enabled", havingValue = "true")
@EnableConfigurationProperties(DruidAopProperties.class)
public class DruidAopStatConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DruidAutoConfiguration.class);
    private static final String DRUID_AOP_PROPERTIES_PREFIX = "spring.datasource.druid.aop";

    @Value("${spring.aop.proxy-target-class:false}")
    private boolean proxyTargetClass;

    @Bean
    public DruidStatInterceptor druidStatInterceptor() {
        return new DruidStatInterceptor();
    }

    @Bean
    public RegexpMethodPointcutAdvisor druidStatAdvisor(DruidAopProperties druidAopProperties,
                                                        DruidStatInterceptor druidStatInterceptor) {
        return new RegexpMethodPointcutAdvisor(druidAopProperties.getPatterns(), druidStatInterceptor);
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator druidStatProxyCreator() {
        log.debug("------ 初始化 Druid Aop 监控 ------");
        DefaultAdvisorAutoProxyCreator druidStatProxyCreator = new DefaultAdvisorAutoProxyCreator();
        druidStatProxyCreator.setProxyTargetClass(proxyTargetClass);
        return druidStatProxyCreator;
    }

    @ConfigurationProperties(DRUID_AOP_PROPERTIES_PREFIX)
    static class DruidAopProperties {

        private boolean enabled;
        private String[] patterns;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String[] getPatterns() {
            return patterns;
        }

        public void setPatterns(String[] patterns) {
            this.patterns = patterns;
        }

    }

}