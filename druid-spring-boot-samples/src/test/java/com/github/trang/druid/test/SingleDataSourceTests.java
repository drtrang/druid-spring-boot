package com.github.trang.druid.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

/**
 * 单数据源测试
 *
 * @author trang
 */
public class SingleDataSourceTests extends BaseTest {

    @Autowired
    private DruidDataSource dataSource;

    @Test
    public void testSingleDataSource() {
        assertEquals(dataSource.getInitialSize(), 1);
        assertEquals(dataSource.getMinIdle(), 1);
        assertEquals(dataSource.getMaxActive(), 10);
        assertEquals(dataSource.getMaxWait(), 30000);
        assertEquals(dataSource.getTimeBetweenEvictionRunsMillis(), 60000);
        assertEquals(dataSource.getMinEvictableIdleTimeMillis(), 300000);
        assertEquals(dataSource.getValidationQuery(), "SELECT 1");
        assertEquals(dataSource.isTestWhileIdle(), true);
        assertEquals(dataSource.isTestOnBorrow(), false);
        assertEquals(dataSource.isTestOnReturn(), false);
        assertEquals(dataSource.getMaxPoolPreparedStatementPerConnectionSize(), 20);
        assertEquals(dataSource.isUseGlobalDataSourceStat(), true);
    }

}