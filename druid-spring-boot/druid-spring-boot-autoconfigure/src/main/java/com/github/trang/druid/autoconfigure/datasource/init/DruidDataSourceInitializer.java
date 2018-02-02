package com.github.trang.druid.autoconfigure.datasource.init;

import com.github.trang.druid.autoconfigure.DruidDataSourceInitializerAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.config.SortedResourcesFactoryBean;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Bean to handle {@link DataSource} initialization by running {@literal schema-*.sql} on {@link PostConstruct}.
 *
 * @author Dave Syer
 * @author Phillip Webb
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 * @author Kazuki Shimizu
 * @see DruidDataSourceInitializerAutoConfiguration
 * @since 1.1.0
 */
@Slf4j
public class DruidDataSourceInitializer {

    private final DataSourceProperties properties;

    private final ApplicationContext applicationContext;

    private List<DataSource> dataSources = new ArrayList<>();

    public DruidDataSourceInitializer(DataSourceProperties properties, ApplicationContext applicationContext) {
        this.properties = properties;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        if (!this.properties.isInitialize()) {
            log.debug("Initialization disabled (not running DDL scripts)");
            return;
        }
        if (this.applicationContext.getBeanNamesForType(DataSource.class, false, false).length > 0) {
            Map<String, DataSource> beans = this.applicationContext.getBeansOfType(DataSource.class, false, false);
            dataSources.addAll(beans.values());
        }
        if (this.dataSources == null || this.dataSources.isEmpty()) {
            log.debug("No DataSource found so not initializing");
            return;
        }
        for (DataSource dataSource : dataSources) {
            runSchemaScripts(dataSource);
        }
    }

    private void runSchemaScripts(DataSource dataSource) {
        List<Resource> scripts = getScripts("spring.datasource.schema", this.properties.getSchema(), "schema");
        if (!scripts.isEmpty()) {
            String username = this.properties.getSchemaUsername();
            String password = this.properties.getSchemaPassword();
            runScripts(dataSource, scripts, username, password);
            runDataScripts(dataSource);
        }
    }

    private void runDataScripts(DataSource dataSource) {
        List<Resource> scripts = getScripts("spring.datasource.data", this.properties.getData(), "data");
        if (!scripts.isEmpty()) {
            String username = this.properties.getDataUsername();
            String password = this.properties.getDataPassword();
            runScripts(dataSource, scripts, username, password);
        }
    }

    private void runScripts(DataSource dataSource, List<Resource> resources, String username, String password) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setContinueOnError(this.properties.isContinueOnError());
        populator.setSeparator(this.properties.getSeparator());
        if (this.properties.getSqlScriptEncoding() != null) {
            populator.setSqlScriptEncoding(this.properties.getSqlScriptEncoding().name());
        }
        for (Resource resource : resources) {
            populator.addScript(resource);
        }
        if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
            dataSource = DataSourceBuilder.create(this.properties.getClassLoader())
                    .driverClassName(this.properties.determineDriverClassName())
                    .url(this.properties.determineUrl())
                    .username(username)
                    .password(password).build();
        }
        DatabasePopulatorUtils.execute(populator, dataSource);
    }

    private List<Resource> getScripts(String propertyName, List<String> resources, String fallback) {
        if (resources != null) {
            return getResources(propertyName, resources, true);
        }
        String platform = this.properties.getPlatform();
        List<String> fallbackResources = new ArrayList<>();
        fallbackResources.add("classpath*:" + fallback + "-" + platform + ".sql");
        fallbackResources.add("classpath*:" + fallback + ".sql");
        return getResources(propertyName, fallbackResources, false);
    }

    private List<Resource> getResources(String propertyName, List<String> locations, boolean validate) {
        List<Resource> resources = new ArrayList<>();
        for (String location : locations) {
            for (Resource resource : doGetResources(location)) {
                if (resource.exists()) {
                    resources.add(resource);
                } else if (validate) {
                    throw new ResourceNotFoundException(propertyName, resource);
                }
            }
        }
        return resources;
    }

    private Resource[] doGetResources(String location) {
        try {
            SortedResourcesFactoryBean factory = new SortedResourcesFactoryBean(this.applicationContext, Collections.singletonList(location));
            factory.afterPropertiesSet();
            return factory.getObject();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to load resources from " + location, ex);
        }
    }

}