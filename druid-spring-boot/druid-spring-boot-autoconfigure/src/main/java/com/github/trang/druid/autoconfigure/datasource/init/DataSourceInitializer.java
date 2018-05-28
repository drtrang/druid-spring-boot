package com.github.trang.druid.autoconfigure.datasource.init;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.source.InvalidConfigurationPropertyValueException;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.jdbc.DataSourceInitializationMode;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.config.SortedResourcesFactoryBean;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.StringUtils;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * Initialize {@link DataSource} List based on a matching {@link DataSourceProperties} config.
 *
 * @author Dave Syer
 * @author Phillip Webb
 * @author Eddú Meléndez
 * @author Stephane Nicoll
 * @author Kazuki Shimizu
 */
public class DataSourceInitializer {

    private static final Log logger = LogFactory.getLog(DataSourceInitializer.class);

    private final List<DruidDataSource> dataSources;

    private final DataSourceProperties properties;

    private final ResourceLoader resourceLoader;

    /**
     * Create a new instance with the {@link DataSource} to initialize and its matching
     * {@link DataSourceProperties configuration}.
     *
     * @param dataSources    the data sources to initialize
     * @param properties     the matching configuration
     * @param resourceLoader the resource loader to use (can be null)
     */
    public DataSourceInitializer(List<DruidDataSource> dataSources, DataSourceProperties properties,
                                 ResourceLoader resourceLoader) {
        this.dataSources = dataSources;
        this.properties = properties;
        this.resourceLoader = (resourceLoader != null ? resourceLoader : new DefaultResourceLoader());
    }

    /**
     * Create a new instance with the {@link DataSource} to initialize and its matching
     * {@link DataSourceProperties configuration}.
     *
     * @param dataSources the data sources to initialize
     * @param properties  the matching configuration
     */
    public DataSourceInitializer(List<DruidDataSource> dataSources, DataSourceProperties properties) {
        this(dataSources, properties, null);
    }

    public List<DruidDataSource> getDataSources() {
        return this.dataSources;
    }

    /**
     * Create the schema if necessary.
     *
     * @return {@code true} if the schema was created
     * @see DataSourceProperties#getSchema()
     */
    public boolean createSchema() {
        List<Resource> scripts = getScripts("spring.datasource.schema",
                this.properties.getSchema(), "schema");
        if (!scripts.isEmpty()) {
            if (!isEnabled()) {
                logger.debug("Initialization disabled (not running DDL scripts)");
                return false;
            }
            String username = this.properties.getSchemaUsername();
            String password = this.properties.getSchemaPassword();
            runScripts(scripts, username, password);
        }
        return !scripts.isEmpty();
    }

    /**
     * Initialize the schema if necessary.
     *
     * @see DataSourceProperties#getData()
     */
    public void initSchema() {
        List<Resource> scripts = getScripts("spring.datasource.data",
                this.properties.getData(), "data");
        if (!scripts.isEmpty()) {
            if (!isEnabled()) {
                logger.debug("Initialization disabled (not running data scripts)");
                return;
            }
            String username = this.properties.getDataUsername();
            String password = this.properties.getDataPassword();
            runScripts(scripts, username, password);
        }
    }

    private boolean isEnabled() {
        DataSourceInitializationMode mode = this.properties.getInitializationMode();
        if (mode == DataSourceInitializationMode.NEVER) {
            return false;
        }
        if (mode == DataSourceInitializationMode.EMBEDDED && !isEmbedded()) {
            return false;
        }
        return true;
    }

    private boolean isEmbedded() {
        try {
            return this.dataSources.stream().allMatch(EmbeddedDatabaseConnection::isEmbedded);
        } catch (Exception ex) {
            logger.debug("Could not determine if datasource is embedded", ex);
            return false;
        }
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
                    throw new InvalidConfigurationPropertyValueException(propertyName,
                            resource, "The specified resource does not exist.");
                }
            }
        }
        return resources;
    }

    private Resource[] doGetResources(String location) {
        try {
            SortedResourcesFactoryBean factory = new SortedResourcesFactoryBean(
                    this.resourceLoader, Collections.singletonList(location));
            factory.afterPropertiesSet();
            return factory.getObject();
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to load resources from " + location, ex);
        }
    }

    private void runScripts(List<Resource> resources, String username, String password) {
        if (resources.isEmpty()) {
            return;
        }
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.setContinueOnError(this.properties.isContinueOnError());
        populator.setSeparator(this.properties.getSeparator());
        if (this.properties.getSqlScriptEncoding() != null) {
            populator.setSqlScriptEncoding(this.properties.getSqlScriptEncoding().name());
        }
        for (Resource resource : resources) {
            populator.addScript(resource);
        }
        List<DruidDataSource> dataSources = this.dataSources;
        boolean isDifferent = (username != null && !this.properties.getUsername().equals(username)) ||
                (password != null && !this.properties.getPassword().equals(password));
        if (username == null) {
            username = this.properties.getUsername();
        }
        if (password == null) {
            password = this.properties.getPassword();
        }
        if (StringUtils.hasText(username) && StringUtils.hasText(password)) {
            for (DataSource dataSource : dataSources) {
                if (isDifferent) {
                    dataSource = DataSourceBuilder.create(this.properties.getClassLoader())
                            .driverClassName(this.properties.determineDriverClassName())
                            .url(this.properties.determineUrl())
                            .username(username)
                            .password(password)
                            .build();
                }
                DatabasePopulatorUtils.execute(populator, dataSource);
            }
        }
    }

}
