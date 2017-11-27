package com.github.trang.druid.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.trang.druid.autoconfigure.DruidDataSourceConfiguration.DruidDataSourceImportSelector;
import com.github.trang.druid.autoconfigure.datasource.DruidDataSource2;
import com.github.trang.druid.autoconfigure.util.CharMatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
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

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;

/**
 * Druid 数据源配置
 *
 * @author trang
 */
@Import(DruidDataSourceImportSelector.class)
@Slf4j
public class DruidDataSourceConfiguration {

    static final String BEAN_NAME = "dataSource";
    static final String BEAN_SUFFIX = "DataSource";
    static final String EMPTY = "";
    static final String POINT = ".";
    static final String PREFIX = "spring.datasource.druid.data-sources.";

    /**
     * 单数据源注册
     *
     * @author trang
     */
    static class SingleDataSourceRegistrar implements ImportBeanDefinitionRegistrar {

        @Override
        public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
            if (!registry.containsBeanDefinition(BEAN_NAME)) {
                registry.registerBeanDefinition(BEAN_NAME, genericDruidBeanDefinition());
            }
        }

    }

    /**
     * 多数据源注册
     *
     * @author trang
     */
    static class DynamicDataSourceRegistrar implements ImportBeanDefinitionRegistrar, EnvironmentAware {

        private RelaxedPropertyResolver resolver;

        @Override
        public void setEnvironment(Environment environment) {
            this.resolver = new RelaxedPropertyResolver(environment, PREFIX);
        }

        @Override
        public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
            resolver.getSubProperties(EMPTY).keySet().stream()
                    .map(key -> key.substring(0, key.indexOf(POINT)))
                    .distinct()
                    .forEach(dataSourceName -> {
                        // 注册 BeanDefinition
                        String camelName = CharMatcher.separatedToCamel().apply(dataSourceName);
                        registry.registerBeanDefinition(camelName, genericDruidBeanDefinition());
                        // 注册以 DataSource 为后缀的别名
                        if (!camelName.toLowerCase().endsWith(BEAN_SUFFIX.toLowerCase())) {
                            registry.registerAlias(camelName, camelName + BEAN_SUFFIX);
                        }
                    });
        }

    }

    /**
     * 构造 BeanDefinition，通过 DruidDataSource2 实现继承 'spring.datasource.druid' 的配置
     *
     * @return BeanDefinition druidBeanDefinition
     */
    static BeanDefinition genericDruidBeanDefinition() {
        return genericBeanDefinition(DruidDataSource2.class)
                .setInitMethodName("init")
                .setDestroyMethodName("close")
                .getBeanDefinition();
    }

    /**
     * DruidDataSource 的 Bean 处理器，将各数据源的自定义配置绑定到 Bean
     *
     * @author trang
     */
    static class DruidDataSourceBeanPostProcessor implements BeanPostProcessor, EnvironmentAware {

        private ConfigurableEnvironment environment;
        private RelaxedPropertyResolver resolver;
        private List<DruidDataSourceCustomizer> customizers;

        @Override
        public void setEnvironment(Environment environment) {
            this.environment = (ConfigurableEnvironment) environment;
            this.resolver = new RelaxedPropertyResolver(environment, PREFIX);
        }

        @Autowired
        public void setCustomizers(ObjectProvider<List<DruidDataSourceCustomizer>> customizersProvider) {
            this.customizers = customizersProvider.getIfAvailable();
        }

        @Override
        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            if (bean instanceof DruidDataSource) {
                // 将 'spring.datasource.druid.data-sources.${name}' 的配置绑定到 Bean
                if (!resolver.getSubProperties(EMPTY).isEmpty()) {
                    PropertySourcesBinder binder = new PropertySourcesBinder(environment);
                    binder.bindTo(PREFIX + beanName, bean);
                }
                // 用户自定义配置
                if (customizers != null && !customizers.isEmpty()) {
                    for (DruidDataSourceCustomizer customizer : customizers) {
                        customizer.customize((DruidDataSource) bean);
                    }
                }
                log.debug("druid {}-data-source init...", beanName);
            }
            return bean;
        }

        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

    }

    /**
     * 数据源选择器
     * 当配置文件中存在 spring.datasource.druid.data-sources 属性时为多数据源
     * 不存在则为单数据源
     *
     * @author trang
     */
    static class DruidDataSourceImportSelector implements ImportSelector, EnvironmentAware {

        private RelaxedPropertyResolver resolver;

        @Override
        public void setEnvironment(Environment environment) {
            this.resolver = new RelaxedPropertyResolver(environment, PREFIX);
        }

        @Override
        public String[] selectImports(AnnotationMetadata metadata) {
            Map<String, Object> properties = resolver.getSubProperties(EMPTY);
            Builder<Class<?>> imposts = Stream.<Class<?>>builder().add(DruidDataSourceBeanPostProcessor.class);
            imposts.add(properties.isEmpty() ? SingleDataSourceRegistrar.class : DynamicDataSourceRegistrar.class);
            return imposts.build().map(Class::getName).toArray(String[]::new);
        }

    }

}