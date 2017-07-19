package com.github.trang.druid.datasource;

/**
 * Druid 多数据源支持，会继承 `spring.datasource.druid` 配置，
 * 并注入 `spring.datasource.druid.${name}` 配置
 *
 * @author trang
 */
public class DruidMultiDataSource extends DruidParentDataSource {

}