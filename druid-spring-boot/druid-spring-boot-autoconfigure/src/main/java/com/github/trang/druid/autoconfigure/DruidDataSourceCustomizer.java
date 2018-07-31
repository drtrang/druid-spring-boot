package com.github.trang.druid.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * DruidDataSource 的回调接口，可以在 DruidDataSource 初始化之前对其进行定制
 *
 * @author trang
 */
public interface DruidDataSourceCustomizer {

    /**
     * 定制化 DruidDataSource
     *
     * @param druidDataSource druid 数据源
     */
    void customize(DruidDataSource druidDataSource);

}