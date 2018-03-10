package com.github.trang.druid.autoconfigure.datasource;

/**
 * Druid 数据源，会继承 AbstractDruidDataSource2 的配置，并注入 'spring.datasource.druid.data-source.${name}' 的配置
 *
 * @author trang
 */
public class DruidDataSource2 extends AbstractDruidDataSource2 {

    private static final long serialVersionUID = 4980333179163868927L;

}