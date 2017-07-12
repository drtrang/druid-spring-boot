package com.github.trang.druid;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import static com.github.trang.druid.DruidProperties.DRUID_DATA_SOURCE_PREFIX;

/**
 * Druid 自定义配置
 *
 * @author trang
 */
@ConfigurationProperties(DRUID_DATA_SOURCE_PREFIX)
public class DruidProperties {

    public static final String DRUID_DATA_SOURCE_PREFIX = "spring.datasource.druid";
    public static final String DRUID_STAT_FILTER_PREFIX = "spring.datasource.druid.stat";
    public static final String DRUID_WALL_FILTER_PREFIX = "spring.datasource.druid.wall";
    public static final String DRUID_CONFIG_FILTER_PREFIX = "spring.datasource.druid.config";
    public static final String DRUID_SLF4J_FILTER_PREFIX = "spring.datasource.druid.slf4j";
    public static final String DRUID_LOG4J_FILTER_PREFIX = "spring.datasource.druid.log4j";
    public static final String DRUID_LOG4J2_FILTER_PREFIX = "spring.datasource.druid.log4j2";
    public static final String DRUID_COMMONS_LOG_FILTER_PREFIX = "spring.datasource.druid.commons-log";
    public static final String DRUID_WEB_STATE_FILTER_PREFIX = "spring.datasource.druid.web-stat-filter";
    public static final String DRUID_STAT_VIEW_SERVLET_PREFIX = "spring.datasource.druid.stat-view-servlet";
    public static final String DRUID_AOP_STAT_PREFIX = "spring.datasource.druid.aop-stat";

    @NestedConfigurationProperty
    private WebStatFilter webStatFilter = new WebStatFilter();
    @NestedConfigurationProperty
    private StatViewServlet statViewServlet = new StatViewServlet();
    @NestedConfigurationProperty
    private AopStat aopStat = new AopStat();

    public WebStatFilter getWebStatFilter() {
        return webStatFilter;
    }

    public void setWebStatFilter(WebStatFilter webStatFilter) {
        this.webStatFilter = webStatFilter;
    }

    public StatViewServlet getStatViewServlet() {
        return statViewServlet;
    }

    public void setStatViewServlet(StatViewServlet statViewServlet) {
        this.statViewServlet = statViewServlet;
    }

    public AopStat getAopStat() {
        return aopStat;
    }

    public void setAopStat(AopStat aopStat) {
        this.aopStat = aopStat;
    }

    /**
     * WebStatFilter 自定义配置
     *
     * @author trang
     */
    public static class WebStatFilter {
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
     * StatViewServlet 自定义配置
     *
     * @author trang
     */
    public static class StatViewServlet {
        // 是否开启 Druid 的数据统计页面，默认否
        private boolean enabled= false;
        // Servlet 的映射规则，默认访问 "http://xxx/druid/"
        private String urlPattern = "/druid/*";
        // Druid 统计页面的登陆用户名
        private String loginUsername;
        // Druid 统计页面的登陆密码
        private String loginPassword;
        // Druid 统计页面的访问白名单，默认允许所有人访问
        private String allow;
        // Druid 统计页面的访问黑名单，优先于白名单
        private String deny;
        // 是否允许清空统计数据，默认否
        private boolean resetEnable;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getUrlPattern() {
            return urlPattern;
        }

        public void setUrlPattern(String urlPattern) {
            this.urlPattern = urlPattern;
        }

        public String getLoginUsername() {
            return loginUsername;
        }

        public void setLoginUsername(String loginUsername) {
            this.loginUsername = loginUsername;
        }

        public String getLoginPassword() {
            return loginPassword;
        }

        public void setLoginPassword(String loginPassword) {
            this.loginPassword = loginPassword;
        }

        public String getAllow() {
            return allow;
        }

        public void setAllow(String allow) {
            this.allow = allow;
        }

        public String getDeny() {
            return deny;
        }

        public void setDeny(String deny) {
            this.deny = deny;
        }

        public boolean isResetEnable() {
            return resetEnable;
        }

        public void setResetEnable(boolean resetEnable) {
            this.resetEnable = resetEnable;
        }

    }

    /**
     * Aop 监控自定义配置
     */
    public static class AopStat {
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