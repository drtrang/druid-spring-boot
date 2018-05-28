package com.github.trang.druid.autoconfigure.datasource.init;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceSchemaCreatedEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * Bean to handle {@link DataSource} initialization by running {@literal schema-*.sql} on
 * {@link InitializingBean#afterPropertiesSet()} and {@literal data-*.sql} SQL scripts on
 * a {@link DataSourceSchemaCreatedEvent}.
 *
 * @author Stephane Nicoll
 * @see org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
 */
public class DataSourceInitializerInvoker
        implements ApplicationListener<DataSourceSchemaCreatedEvent>, InitializingBean {

    private static final Log logger = LogFactory.getLog(DataSourceInitializerInvoker.class);

    private final List<DruidDataSource> dataSources;

    private final DataSourceProperties properties;

    private final ApplicationContext applicationContext;

    private DataSourceInitializer dataSourceInitializer;

    private boolean initialized;

    public DataSourceInitializerInvoker(ObjectProvider<List<DruidDataSource>> dataSourcesProvider,
                                        DataSourceProperties properties, ApplicationContext applicationContext) {
        this.dataSources = dataSourcesProvider.getIfAvailable(ArrayList::new);
        this.properties = properties;
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        DataSourceInitializer initializer = getDataSourceInitializer();
        if (initializer != null) {
            boolean schemaCreated = this.dataSourceInitializer.createSchema();
            if (schemaCreated) {
                initialize(initializer);
            }
        }
    }

    private void initialize(DataSourceInitializer initializer) {
        try {
            initializer.getDataSources().stream()
                    .map(DataSourceSchemaCreatedEvent::new)
                    .forEach(this.applicationContext::publishEvent);
            // The listener might not be registered yet, so don't rely on it.
            if (!this.initialized) {
                this.dataSourceInitializer.initSchema();
                this.initialized = true;
            }
        } catch (IllegalStateException ex) {
            logger.warn("Could not send event to complete DataSource initialization ("
                    + ex.getMessage() + ")");
        }
    }

    @Override
    public void onApplicationEvent(DataSourceSchemaCreatedEvent event) {
        // NOTE the event can happen more than once and
        // the event datasource is not used here
        DataSourceInitializer initializer = getDataSourceInitializer();
        if (!this.initialized && initializer != null) {
            initializer.initSchema();
            this.initialized = true;
        }
    }

    private DataSourceInitializer getDataSourceInitializer() {
        if (this.dataSourceInitializer == null) {
            List<DruidDataSource> dataSources = this.dataSources;
            if (!dataSources.isEmpty()) {
                this.dataSourceInitializer = new DataSourceInitializer(dataSources, this.properties, this.applicationContext);
            }
        }
        return this.dataSourceInitializer;
    }

}
