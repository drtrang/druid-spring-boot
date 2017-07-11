package com.github.trang.druid;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Druid 自定义配置
 *
 * @author trang
 */
@ConfigurationProperties("spring.datasource.druid.web")
public class DruidStatProperties {

    private StatViewServlet statViewServlet = new StatViewServlet();
    private WebStatFilter webStatFilter = new WebStatFilter();

    public StatViewServlet getStatViewServlet() {
        return statViewServlet;
    }

    public void setStatViewServlet(StatViewServlet statViewServlet) {
        this.statViewServlet = statViewServlet;
    }

    public WebStatFilter getWebStatFilter() {
        return webStatFilter;
    }

    public void setWebStatFilter(WebStatFilter webStatFilter) {
        this.webStatFilter = webStatFilter;
    }

    public static class StatViewServlet {

        private boolean enabled;
        private String urlPattern = "/druid/*";
        private String allow;
        private String deny;
        private String loginUsername;
        private String loginPassword;
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

        public boolean getResetEnable() {
            return resetEnable;
        }

        public void setResetEnable(boolean resetEnable) {
            this.resetEnable = resetEnable;
        }

    }

    public static class WebStatFilter {

        private boolean enabled;
        private String urlPattern = "/*";
        private String exclusions = "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*";
        private String sessionStatMaxCount;
        private boolean sessionStatEnable;
        private String principalSessionName;
        private String principalCookieName;
        private boolean profileEnable;

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

        public String getExclusions() {
            return exclusions;
        }

        public void setExclusions(String exclusions) {
            this.exclusions = exclusions;
        }

        public String getSessionStatMaxCount() {
            return sessionStatMaxCount;
        }

        public void setSessionStatMaxCount(String sessionStatMaxCount) {
            this.sessionStatMaxCount = sessionStatMaxCount;
        }

        public boolean getSessionStatEnable() {
            return sessionStatEnable;
        }

        public void setSessionStatEnable(boolean sessionStatEnable) {
            this.sessionStatEnable = sessionStatEnable;
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

        public boolean getProfileEnable() {
            return profileEnable;
        }

        public void setProfileEnable(boolean profileEnable) {
            this.profileEnable = profileEnable;
        }

    }

}