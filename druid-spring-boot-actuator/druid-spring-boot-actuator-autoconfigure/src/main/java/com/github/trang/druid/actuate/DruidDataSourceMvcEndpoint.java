package com.github.trang.druid.actuate;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.web.annotation.EndpointWebExtension;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.alibaba.druid.stat.DruidStatService;

/**
 * 将 {@link DruidDataSourceEndpoint} 适配成 {@link MvcEndpoint}
 *
 * @author trang
 */
@ConfigurationProperties(prefix = "endpoints.druid")
@EndpointWebExtension(endpoint = DruidDataSourceEndpoint.class)
public class DruidDataSourceMvcEndpoint {

    private static final DruidStatService statService = DruidStatService.getInstance();

    private final DruidDataSourceEndpoint delegate;
    private final long timeout;
    private Lock lock = new ReentrantLock();

    public DruidDataSourceMvcEndpoint(DruidDataSourceEndpoint delegate) {
        this.delegate = delegate;
        this.timeout = TimeUnit.SECONDS.toMillis(10);
    }

    public DruidDataSourceMvcEndpoint(DruidDataSourceEndpoint delegate, long timeout) {
        this.delegate = delegate;
        this.timeout = timeout;
    }

    @ReadOperation
    public String handle(@Selector String name) {
        String temp = name;
        if (name.contains(".")) {
            temp = name.substring(0, name.indexOf("."));
        }
        name = "/" + temp + ".json";
        return statService.service(name);
    }

}