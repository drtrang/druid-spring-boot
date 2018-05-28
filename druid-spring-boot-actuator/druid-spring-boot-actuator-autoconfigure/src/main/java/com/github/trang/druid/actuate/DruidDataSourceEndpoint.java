package com.github.trang.druid.actuate;

import java.util.List;
import java.util.Map;

import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.alibaba.druid.stat.DruidStatManagerFacade;

/**
 * 将 Druid 的信息暴露出来
 *
 * @author trang
 */
@ConfigurationProperties(prefix = "endpoints.druid")
public class DruidDataSourceEndpoint extends AbstractEndpoint<List<Map<String, Object>>> {

    public DruidDataSourceEndpoint() {
        super("druid");
    }

    private static final DruidStatManagerFacade STAT_MANAGER = DruidStatManagerFacade.getInstance();

    @Override
    public List<Map<String, Object>> invoke() {
        return STAT_MANAGER.getDataSourceStatDataList();
    }

}