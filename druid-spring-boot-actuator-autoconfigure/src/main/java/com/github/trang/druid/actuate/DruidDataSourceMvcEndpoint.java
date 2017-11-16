package com.github.trang.druid.actuate;

import com.alibaba.druid.stat.DruidStatService;
import org.springframework.boot.actuate.endpoint.mvc.ActuatorMediaTypes;
import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.boot.actuate.endpoint.mvc.HypermediaDisabled;
import org.springframework.boot.actuate.endpoint.mvc.MvcEndpoint;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 将 {@link DruidDataSourceEndpoint} 适配成 {@link MvcEndpoint}
 *
 * @author trang
 */
@ConfigurationProperties(prefix = "endpoints.druid")
public class DruidDataSourceMvcEndpoint extends EndpointMvcAdapter {

    private static final DruidStatService statService = DruidStatService.getInstance();

    private final long timeout;
    private final Lock lock = new ReentrantLock();

    public DruidDataSourceMvcEndpoint(DruidDataSourceEndpoint delegate) {
        super(delegate);
        this.timeout = TimeUnit.SECONDS.toMillis(10);
    }

    public DruidDataSourceMvcEndpoint(DruidDataSourceEndpoint delegate, long timeout) {
        super(delegate);
        this.timeout = timeout;
    }

    @GetMapping(value = "/{name:.*}", produces = {ActuatorMediaTypes.APPLICATION_ACTUATOR_V1_JSON_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    @HypermediaDisabled
    public String handle(@PathVariable String name) {
        String temp = name;
        if (name.contains(".")) {
            temp = name.substring(0, name.indexOf("."));
        }
        name = "/" + temp + ".json";
        return statService.service(name);
    }

}