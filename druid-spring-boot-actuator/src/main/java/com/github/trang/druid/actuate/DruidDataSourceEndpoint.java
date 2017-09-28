package com.github.trang.druid.actuate;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.DruidStatManagerFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author trang
 */
public class DruidDataSourceEndpoint extends AbstractEndpoint<Set<Health>> {

    public DruidDataSourceEndpoint() {
        super("druid");
    }

    @Autowired
    private List<DruidDataSource> dataSourceList;

    @Override
    public Set<Health> invoke() {
        if (dataSourceList == null || dataSourceList.isEmpty()) {
            return Collections.singleton(Health.outOfService()
                    .withException(new IllegalStateException("Can not find bean with type 'DruidDataSource'!"))
                    .build());
        }
        DruidStatManagerFacade statManager = DruidStatManagerFacade.getInstance();
        return statManager.getDataSourceStatDataList().stream()
                .map(ds -> new Health.Builder(Status.UP, ds).build())
                .collect(Collectors.toSet());

//        return dataSourceList.stream()
//                .map(DruidDataSource::getName)
//                .map(statManager::getDruidDataSourceByName)
//                .map(m -> new Health.Builder(Status.UP, m).build())
//                .collect(Collectors.toSet());
//        return dataSourceList.stream()
//                .map(gson::toJson)
//                .map(this::readValueAsMap)
//                .map(m -> new Health.Builder(Status.UP, m).build())
//                .collect(Collectors.toSet());
    }

}