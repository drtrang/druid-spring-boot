package com.github.trang.druid.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.trang.druid.autoconfigure.DruidDataSourceConfiguration.DataSourceImportSelector;
import com.github.trang.druid.autoconfigure.datasource.DruidDataSource2;
import com.github.trang.druid.autoconfigure.util.CharMatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.bind.PropertySourcesBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * Druid 数据源配置
 *
 * @author trang
 */
@Import(DataSourceImportSelector.class)
@Slf4j
public class DruidDataSourceConfiguration {

    /**
     * 单数据源注册
     *
     * @author trang
     */
    static class SingleDataSourceRegister implements ImportBeanDefinitionRegistrar {

        static final String BEAN_NAME = "dataSource";

        @Override
        public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
            if (!registry.containsBeanDefinition(BEAN_NAME)) {
                BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(DruidDataSource2.class)
                        .setInitMethodName("init")
                        .setDestroyMethodName("close")
                        .getBeanDefinition();
                registry.registerBeanDefinition(BEAN_NAME, beanDefinition);
                log.debug("druid data-source init...");
            }
        }
    }

    /**
     * 多数据源注册
     *
     * @author trang
     */
    static class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

        private static final String BEAN_SUFFIX = "DataSource";
        private RelaxedPropertyResolver resolver;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
            resolver.getSubProperties("").keySet().stream()
                    .map(key -> key.substring(0, key.indexOf(".")))
                    .distinct()
                    .forEach(dataSourceName -> {
                        // 构造 BeanDefinition，通过 DruidDataSource2 实现继承 'spring.datasource.druid' 的配置
                        BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(DruidDataSource2.class)
                                .setInitMethodName("init")
                                .setDestroyMethodName("close")
                                .getBeanDefinition();
                        // 注册 BeanDefinition
                        registry.registerBeanDefinition(dataSourceName, beanDefinition);
                        // 注册驼峰别名
                        String camelAlias = CharMatcher.separatedToCamel().apply(dataSourceName);
                        registry.registerAlias(dataSourceName, camelAlias);
                        // 注册以 DataSource 为后缀的别名
                        String otherAlias;
                        if (camelAlias.toLowerCase().endsWith(BEAN_SUFFIX.toLowerCase())) {
                            if (!camelAlias.endsWith(BEAN_SUFFIX)) {
                                otherAlias = camelAlias.substring(0, camelAlias.toLowerCase().indexOf(BEAN_SUFFIX.toLowerCase()));
                                otherAlias = otherAlias + BEAN_SUFFIX;
                                registry.registerAlias(dataSourceName, otherAlias);
                            }
                        } else {
                            otherAlias = camelAlias + BEAN_SUFFIX;
                            registry.registerAlias(dataSourceName, otherAlias);
                        }
                        log.debug("druid {}-data-source init...", dataSourceName);
                    });
        }

        @Override
        public void setEnvironment(Environment environment) {
            this.resolver = new RelaxedPropertyResolver(environment, "spring.datasource.druid.data-sources.");
        }

    }

    /**
     * 数据源选择器
     * 当配置文件中存在 spring.datasource.druid.data-sources 属性时为多数据源
     * 不存在则为单数据源
     *
     * @author trang
     */
    static class DataSourceImportSelector implements ImportSelector, EnvironmentAware {

        private ConfigurableEnvironment environment;

        @Override
        public String[] selectImports(AnnotationMetadata metadata) {
            RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment, "spring.datasource.druid.");
            Map<String, Object> properties = resolver.getSubProperties("data-sources");
            if (properties.isEmpty()) {
                return new String[]{SingleDataSourceRegister.class.getName()};
            }
            return new String[]{DynamicDataSourceRegister.class.getName(),
                    DataSourceBeanPostProcessor.class.getName()};
        }

        @Override
        public void setEnvironment(Environment environment) {
            this.environment = (ConfigurableEnvironment) environment;
        }

    }

    /**
     * DataSource Bean 处理器
     *
     * @author trang
     */
    static class DataSourceBeanPostProcessor implements BeanPostProcessor, EnvironmentAware {

        private ConfigurableEnvironment environment;

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof DruidDataSource) {
                // 将 'spring.datasource.druid.data-sources.${name}' 的配置绑定到 Bean
                PropertySourcesBinder binder = new PropertySourcesBinder(environment);
                binder.bindTo("spring.datasource.druid.data-sources." + beanName, bean);
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        @Override
        public void setEnvironment(Environment environment) {
            this.environment = (ConfigurableEnvironment) environment;
        }

    }

}