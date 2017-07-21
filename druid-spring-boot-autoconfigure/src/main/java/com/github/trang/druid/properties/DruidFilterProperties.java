package com.github.trang.druid.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.github.trang.druid.properties.DruidFilterProperties.DruidConfigFilterProperties.DRUID_CONFIG_FILTER_PREFIX;

/**
 * Dubbo Filter 自定义配置
 *
 * @author trang
 */
public class DruidFilterProperties {

    @ConfigurationProperties(DRUID_CONFIG_FILTER_PREFIX)
    public static class DruidConfigFilterProperties {

        public static final String DRUID_CONFIG_FILTER_PREFIX = "spring.datasource.druid.config";

        private boolean enabled;
        private String file;
        private String key;


        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

    }

}