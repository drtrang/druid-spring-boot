package com.github.trang.druid.autoconfigure;

import com.alibaba.druid.filter.config.ConfigFilter;
import com.alibaba.druid.filter.logging.CommonsLogFilter;
import com.alibaba.druid.filter.logging.Log4j2Filter;
import com.alibaba.druid.filter.logging.Log4jFilter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.github.trang.druid.autoconfigure.datasource.DruidDataSource2;
import com.github.trang.druid.autoconfigure.properties.DruidDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.bind.PropertySourcesBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.trang.druid.autoconfigure.properties.DruidDataSourceProperties.*;

/**
 * Druid 连接池的自动配置
 *
 * @author trang
 */
@Configuration
@ConditionalOnClass(DruidDataSource.class)
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties({DataSourceProperties.class, DruidDataSourceProperties.class})
@Import({DruidServletConfiguration.class, DruidStatConfiguration.class})
@Slf4j
public class DruidAutoConfiguration implements BeanFactoryAware, EnvironmentAware {

    private DefaultListableBeanFactory beanFactory;
    private ConfigurableEnvironment environment;

    @Bean
    @ConditionalOnProperty(prefix = DRUID_STAT_FILTER_PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
    @ConfigurationProperties(DRUID_STAT_FILTER_PREFIX)
    public StatFilter statFilter() {
        log.debug("druid stat-filter init...");
        return new StatFilter();
    }

    @Bean
    @ConditionalOnProperty(prefix = DRUID_WALL_FILTER_PREFIX, name = "enabled", havingValue = "true")
    @ConfigurationProperties(DRUID_WALL_CONFIG_PREFIX)
    public WallConfig wallConfig() {
        return new WallConfig();
    }

    @Bean
    @ConditionalOnProperty(prefix = DRUID_WALL_FILTER_PREFIX, name = "enabled", havingValue = "true")
    @ConfigurationProperties(DRUID_WALL_FILTER_PREFIX)
    public WallFilter wallFilter(WallConfig wallConfig) {
        log.debug("druid wall-filter init...");
        WallFilter filter = new WallFilter();
        filter.setConfig(wallConfig);
        return filter;
    }

    @Bean
    @ConditionalOnProperty(prefix = DRUID_CONFIG_FILTER_PREFIX, name = "enabled", havingValue = "true")
    public ConfigFilter configFilter() {
        log.debug("druid config-filter init...");
        return new ConfigFilter();
    }

    @Bean
    @ConditionalOnProperty(prefix = DRUID_SLF4J_FILTER_PREFIX, name = "enabled", havingValue = "true")
    @ConfigurationProperties(DRUID_SLF4J_FILTER_PREFIX)
    public Slf4jLogFilter slf4jLogFilter() {
        log.debug("druid slf4j-filter init...");
        return new Slf4jLogFilter();
    }

    @Bean
    @ConditionalOnProperty(prefix = DRUID_LOG4J_FILTER_PREFIX, name = "enabled", havingValue = "true")
    @ConfigurationProperties(DRUID_LOG4J_FILTER_PREFIX)
    public Log4jFilter log4jFilter() {
        log.debug("druid log4j-filter init...");
        return new Log4jFilter();
    }

    @Bean
    @ConditionalOnProperty(prefix = DRUID_LOG4J2_FILTER_PREFIX, name = "enabled", havingValue = "true")
    @ConfigurationProperties(DRUID_LOG4J2_FILTER_PREFIX)
    public Log4j2Filter log4j2Filter() {
        log.debug("druid log4j2-filter init...");
        return new Log4j2Filter();
    }

    @Bean
    @ConditionalOnProperty(prefix = DRUID_COMMONS_LOG_FILTER_PREFIX, name = "enabled", havingValue = "true")
    @ConfigurationProperties(DRUID_COMMONS_LOG_FILTER_PREFIX)
    public CommonsLogFilter commonsLogFilter() {
        log.debug("druid commons-log-filter init...");
        return new CommonsLogFilter();
    }

    /**
     * 自动注入单数据源
     *
     * @condition 1. BeanFactory 中不存在 DruidDataSource 类型的 bean
     * @condition 2. BeanFactory 中不存在 'dataSource' 名称的 bean
     * @condition 3. 配置文件中不存在以 'spring.datasource.druid.data-source' 开头的属性
     * @return DruidDataSource dataSource
     */
    @Bean(initMethod = "init", destroyMethod = "close")
    @ConditionalOnMissingBean(value = DataSource.class, name = "dataSource")
    @Conditional(DataSourceCondition.class)
    public DruidDataSource dataSource() {
        log.debug("druid data-source init...");
        return new DruidDataSource2();
    }

    /**
     * 自动注入多数据源
     *
     * @condition 1. BeanFactory 中不存在 DruidDataSource 类型的 bean
     * @condition 2. 配置文件中存在以 'spring.datasource.druid.data-source' 开头的属性
     * @return Map dataSourceMap/druidDataSourceMap
     */
    @Bean
    @Conditional(DataSourcesCondition.class)
    public Map<String, DruidDataSource> dataSourceMap(DruidDataSourceProperties druidDataSourceProperties) {
        druidDataSourceProperties.getDataSources().forEach((dataSourceName, dataSourceConfig) -> {
            // 构造 BeanDefinition，通过 DruidDataSource2 实现继承 'spring.datasource.druid' 的配置
            BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(DruidDataSource2.class)
                    .setInitMethodName("init")
                    .setDestroyMethodName("close")
                    .getBeanDefinition();
            // 注册 BeanDefinition
            beanFactory.registerBeanDefinition(dataSourceName, beanDefinition);
            // 注册 Alias
            beanFactory.registerAlias(dataSourceName, dataSourceName + "DataSource");
            // 将 'spring.datasource.druid.data-sources.$dataSourceName' 的配置绑定到 Bean
            DruidDataSource2 dataSource = beanFactory.getBean(dataSourceName, DruidDataSource2.class);
            PropertySourcesBinder propertySourcesBinder = new PropertySourcesBinder(environment);
            propertySourcesBinder.bindTo("spring.datasource.druid.data-sources." + dataSourceName, dataSource);
            log.debug("druid {}-data-source init...", dataSourceName);
        });
        return new HashMap<>(beanFactory.getBeansOfType(DruidDataSource2.class));
    }

    /**
     * 为了便于使用，增加数据源集合，可直接注入使用
     * 如需使用 Set、Array 请注入 Set<DruidDataSource2> 或 DruidDataSource2[]
     *
     * @param dataSourceMap 多数据源
     * @return List<DruidDataSource> dataSourceList
     */
    @Bean
    @ConditionalOnBean(name = "dataSourceMap")
    @DependsOn("dataSourceMap")
    public List<DruidDataSource> dataSourceList(Map<String, DruidDataSource> dataSourceMap) {
        return new ArrayList<>(dataSourceMap.values());
    }

    static class DataSourceCondition extends SpringBootCondition {

        @Override
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(context.getEnvironment(), "spring.datasource.druid.");
            Map<String, Object> properties = resolver.getSubProperties("data-sources");
            if (!properties.isEmpty()) {
                return ConditionOutcome.noMatch("find 'data-sources' properties");
            }
            return ConditionOutcome.match();
        }

    }

    static class DataSourcesCondition extends SpringBootCondition {

        @Override
        public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
            RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(context.getEnvironment(), "spring.datasource.druid.");
            Map<String, Object> properties = resolver.getSubProperties("data-sources");
            if (properties.isEmpty()) {
                return ConditionOutcome.noMatch("can't find 'data-sources' properties");
            }
            return ConditionOutcome.match();
        }

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory)beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment)environment;
    }

}