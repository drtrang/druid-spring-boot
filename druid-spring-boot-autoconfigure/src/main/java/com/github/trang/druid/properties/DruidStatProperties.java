package com.github.trang.druid.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Druid 监控配置
 *
 * @author trang
 */
public class DruidStatProperties {

    public static final String DRUID_WEB_STAT_PREFIX = "spring.datasource.druid.web-stat";
    public static final String DRUID_AOP_STAT_PREFIX = "spring.datasource.druid.aop-stat";

    /**
     * WebStat 自定义配置
     *
     * @author trang
     */
    @ConfigurationProperties(DRUID_WEB_STAT_PREFIX)
    public static class DruidWebStatProperties {

        // 是否开启 web-jdbc 监控，默认否
        private boolean enabled;
        // 过滤器 url 的映射规则
        private String urlPatterns = "/*";
        // 过滤器 url 的排除规则
        private String exclusions = "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*";
        // 是否开启 session 统计，默认否
        private boolean sessionStatEnable;
        // session 统计的最大值，默认值 1000
        private Integer sessionStatMaxCount = 1000;
        // 配置当前 session 的用户
        private String principalSessionName;
        // 配置当前 cookie 的用户
        private String principalCookieName;
        // 是否开启监控单个 url 调用的 sql 列表，默认是
        private boolean profileEnable = true;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getUrlPatterns() {
            return urlPatterns;
        }

        public void setUrlPatterns(String urlPatterns) {
            this.urlPatterns = urlPatterns;
        }

        public String getExclusions() {
            return exclusions;
        }

        public void setExclusions(String exclusions) {
            this.exclusions = exclusions;
        }

        public boolean isSessionStatEnable() {
            return sessionStatEnable;
        }

        public void setSessionStatEnable(boolean sessionStatEnable) {
            this.sessionStatEnable = sessionStatEnable;
        }

        public Integer getSessionStatMaxCount() {
            return sessionStatMaxCount;
        }

        public void setSessionStatMaxCount(Integer sessionStatMaxCount) {
            this.sessionStatMaxCount = sessionStatMaxCount;
        }

        public String getPrincipalSessionName() {
            return principalSessionName;
        }

        public void setPrincipalSessionName(String principalSessionName) {
            this.principalSessionName = principalSessionName;
        }

        public String getPrincipalCookieName() {
            return principalCookieName;
        }

        public void setPrincipalCookieName(String principalCookieName) {
            this.principalCookieName = principalCookieName;
        }

        public boolean isProfileEnable() {
            return profileEnable;
        }

        public void setProfileEnable(boolean profileEnable) {
            this.profileEnable = profileEnable;
        }

    }

    /**
     * Aop 监控自定义配置
     */
    @ConfigurationProperties(DRUID_AOP_STAT_PREFIX)
    public static class DruidAopStatProperties {

        // 是否开启基于 aop 的监控，默认否
        private boolean enabled;
        // aop 拦截规则
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