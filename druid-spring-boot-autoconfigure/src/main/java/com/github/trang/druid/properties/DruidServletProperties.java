package com.github.trang.druid.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Druid 数据展示配置
 *
 * @author trang
 */
public class DruidServletProperties {

    public static final String DRUID_STAT_VIEW_SERVLET_PREFIX = "spring.datasource.druid.stat-view-servlet";

    /**
     * StatViewServlet 自定义配置
     *
     * @author trang
     */
    @ConfigurationProperties(DRUID_STAT_VIEW_SERVLET_PREFIX)
    public static class DruidStatViewServletProperties {

        // 是否开启 Druid 的数据统计页面，默认否
        private boolean enabled;
        // Servlet 的映射规则，默认访问 "http://xxx/druid/"
        private String urlMappings = "/druid/*";
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

        public String getUrlMappings() {
            return urlMappings;
        }

        public void setUrlMappings(String urlMappings) {
            this.urlMappings = urlMappings;
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

}