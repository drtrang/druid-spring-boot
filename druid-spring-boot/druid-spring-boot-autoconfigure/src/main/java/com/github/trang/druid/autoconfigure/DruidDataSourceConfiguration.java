package com.github.trang.druid.autoconfigure;

import com.github.trang.druid.autoconfigure.datasource.DruidDataSource2;
import com.github.trang.druid.autoconfigure.properties.DruidDataSourceProperties;
import com.github.trang.druid.autoconfigure.util.CharMatcher;
import com.github.trang.druid.autoconfigure.DruidDataSourceConfiguration.DataSourceImportSelector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.bind.PropertySourcesBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Map;

/**
 * @author trang
 */
@Import({DataSourceImportSelector.class})
@Slf4j
public class DruidDataSourceConfiguration {

    public static class SingleDataSourceRegister implements ImportBeanDefinitionRegistrar {

        static final String BEAN_NAME = "dataSource";

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
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

    public static class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, BeanFactoryAware, EnvironmentAware {

        private DruidDataSourceProperties druidDataSourceProperties;
        private DefaultListableBeanFactory beanFactory;
        private ConfigurableEnvironment environment;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            druidDataSourceProperties.getDataSources().forEach((dataSourceName, dataSourceConfig) -> {
                // 构造 BeanDefinition，通过 DruidDataSource2 实现继承 'spring.datasource.druid' 的配置
                BeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(DruidDataSource2.class)
                        .setInitMethodName("init")
                        .setDestroyMethodName("close")
                        .getBeanDefinition();
                // 注册 BeanDefinition
                String beanName = CharMatcher.separatedToCamel().apply(dataSourceName);
                registry.registerBeanDefinition(beanName, beanDefinition);
                // 将 'spring.datasource.druid.data-sources.${name}' 的配置绑定到 Bean
                DruidDataSource2 dataSource = beanFactory.getBean(beanName, DruidDataSource2.class);
                PropertySourcesBinder propertySourcesBinder = new PropertySourcesBinder(environment);
                propertySourcesBinder.bindTo("spring.datasource.druid.data-sources." + dataSourceName, dataSource);
                log.debug("druid {}-data-source init...", dataSourceName);
            });
        }

        @Autowired
        public void setDruidDataSourceProperties(DruidDataSourceProperties properties) {
            this.druidDataSourceProperties = properties;
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = (DefaultListableBeanFactory) beanFactory;
        }

        @Override
        public void setEnvironment(Environment environment) {
            this.environment = (ConfigurableEnvironment) environment;
        }

    }

    static class DataSourceImportSelector implements ImportSelector, EnvironmentAware {

        private ConfigurableEnvironment environment;

        @Override
        public String[] selectImports(AnnotationMetadata metadata) {
            RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(environment, "spring.datasource.druid.");
            Map<String, Object> properties = resolver.getSubProperties("data-sources");
            if (properties.isEmpty()) {
                return new String[]{DruidDataSourceProperties.class.getName(), SingleDataSourceRegister.class.getName()};
            }
            return new String[]{DruidDataSourceProperties.class.getName(), DynamicDataSourceRegister.class.getName()};
        }

        @Override
        public void setEnvironment(Environment environment) {
            this.environment = (ConfigurableEnvironment) environment;
        }

    }

}