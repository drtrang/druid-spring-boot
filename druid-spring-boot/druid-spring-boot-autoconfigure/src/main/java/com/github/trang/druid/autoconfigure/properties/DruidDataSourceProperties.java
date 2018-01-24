package com.github.trang.druid.autoconfigure.properties;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.HashMap;
import java.util.Map;

import static com.github.trang.druid.autoconfigure.properties.DruidDataSourceProperties.DRUID_DATA_SOURCE_PREFIX;

/**
 * Druid 自定义配置
 *
 * @author trang
 */
@ConfigurationProperties(prefix = DRUID_DATA_SOURCE_PREFIX)
@Getter @Setter
public class DruidDataSourceProperties {

    public static final String DRUID_DATA_SOURCE_PREFIX = "spring.datasource.druid";
    public static final String DRUID_STAT_FILTER_PREFIX = "spring.datasource.druid.stat";
    public static final String DRUID_WALL_FILTER_PREFIX = "spring.datasource.druid.wall";
    public static final String DRUID_WALL_CONFIG_PREFIX = "spring.datasource.druid.wall.config";
    public static final String DRUID_SLF4J_FILTER_PREFIX = "spring.datasource.druid.slf4j";
    public static final String DRUID_LOG4J_FILTER_PREFIX = "spring.datasource.druid.log4j";
    public static final String DRUID_LOG4J2_FILTER_PREFIX = "spring.datasource.druid.log4j2";
    public static final String DRUID_COMMONS_LOG_FILTER_PREFIX = "spring.datasource.druid.commons-log";
    public static final String DRUID_CONFIG_FILTER_PREFIX = "spring.datasource.druid.config";
    public static final String DRUID_STAT_VIEW_SERVLET_PREFIX = "spring.datasource.druid.stat-view-servlet";
    public static final String DRUID_WEB_STAT_PREFIX = "spring.datasource.druid.web-stat";
    public static final String DRUID_AOP_STAT_PREFIX = "spring.datasource.druid.aop-stat";

    /** druid config-filter 配置 */
    @NestedConfigurationProperty
    DruidConfigFilterProperties config = new DruidConfigFilterProperties();
    /** druid stat-view-servlet 配置 */
    @NestedConfigurationProperty
    DruidStatViewServletProperties statViewServlet = new DruidStatViewServletProperties();
    /** druid web-stat 配置 */
    @NestedConfigurationProperty
    DruidWebStatProperties webStat = new DruidWebStatProperties();
    /** druid aop-stat 配置 */
    @NestedConfigurationProperty
    DruidAopStatProperties aopStat = new DruidAopStatProperties();

    Map<String, DruidDataSource> dataSources = new HashMap<>(16);

    @Getter
    @Setter
    public static class DruidConfigFilterProperties {
        /** 是否开启 druid config-filter，默认否 */
        private boolean enabled;
        /** 密钥地址 */
        private String file;
        /** 加密密钥 */
        private String key;
    }

    @Getter
    @Setter
    public static class DruidStatViewServletProperties {
        /** 是否开启 druid 的数据统计页面，默认否 */
        private boolean enabled;
        /** servlet 的映射规则，默认访问 "http:/**xxx/druid/" */
        private String urlMappings = "/druid/*";
        /** druid 统计页面的登陆用户名 */
        private String loginUsername;
        /** druid 统计页面的登陆密码 */
        private String loginPassword;
        /** druid 统计页面的访问白名单，默认允许所有人访问 */
        private String allow;
        /** druid 统计页面的访问黑名单，优先于白名单 */
        private String deny;
        /** 是否允许清空统计数据，默认否 */
        private boolean resetEnable;
    }

    @Getter
    @Setter
    public static class DruidWebStatProperties {
        /** 是否开启 web-jdbc 监控，默认否 */
        private boolean enabled;
        /** 过滤器 url 的映射规则 */
        private String urlPatterns = "/*";
        /** 过滤器 url 的排除规则 */
        private String exclusions = "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*";
        /** 是否开启 session 统计，默认否 */
        private boolean sessionStatEnable;
        /** session 统计的最大值，默认值 1000 */
        private Integer sessionStatMaxCount = 1000;
        /** 配置当前 session 的用户 */
        private String principalSessionName;
        /** 配置当前 cookie 的用户 */
        private String principalCookieName;
        /** 是否开启监控单个 url 调用的 sql 列表，默认是 */
        private boolean profileEnable = true;
    }

    @Getter
    @Setter
    public static class DruidAopStatProperties {
        /** 是否开启基于 aop 的监控，默认否 */
        private boolean enabled;
        /** aop 拦截规则 */
        private String[] patterns;
    }

}