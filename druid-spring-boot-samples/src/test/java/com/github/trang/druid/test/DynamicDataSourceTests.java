package com.github.trang.druid.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.trang.druid.config.DynamicDataSource;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertEquals;

/**
 * 动态数据源测试
 *
 * @author trang
 */
@ActiveProfiles("dynamic")
public class DynamicDataSourceTests extends BaseTest {

    @Autowired
    private DruidDataSource firstDataSource;
    @Autowired
    private DruidDataSource secondDataSource;
    @Autowired
    private DynamicDataSource dataSource;

    @Test
    public void testFirstDataSource() {
        assertEquals(1, firstDataSource.getInitialSize());
        assertEquals(1, firstDataSource.getMinIdle());
        assertEquals(50, firstDataSource.getMaxActive());
        assertEquals(30000, firstDataSource.getMaxWait());
        assertEquals(60000, firstDataSource.getTimeBetweenEvictionRunsMillis());
        assertEquals(300000, firstDataSource.getMinEvictableIdleTimeMillis());
        assertEquals("SELECT 1", firstDataSource.getValidationQuery());
        assertEquals(true, firstDataSource.isTestWhileIdle());
        assertEquals(false, firstDataSource.isTestOnBorrow());
        assertEquals(false, firstDataSource.isTestOnReturn());
        assertEquals(50, firstDataSource.getMaxPoolPreparedStatementPerConnectionSize());
        assertEquals(true, firstDataSource.isUseGlobalDataSourceStat());
    }

    @Test
    public void testSecondDataSource() {
        assertEquals(1, secondDataSource.getInitialSize());
        assertEquals(1, secondDataSource.getMinIdle());
        assertEquals(25, secondDataSource.getMaxActive());
        assertEquals(30000, secondDataSource.getMaxWait());
        assertEquals(60000, secondDataSource.getTimeBetweenEvictionRunsMillis());
        assertEquals(300000, secondDataSource.getMinEvictableIdleTimeMillis());
        assertEquals("SELECT 1", secondDataSource.getValidationQuery());
        assertEquals(true, secondDataSource.isTestWhileIdle());
        assertEquals(false, secondDataSource.isTestOnBorrow());
        assertEquals(false, secondDataSource.isTestOnReturn());
        assertEquals(25, secondDataSource.getMaxPoolPreparedStatementPerConnectionSize());
        assertEquals(true, secondDataSource.isUseGlobalDataSourceStat());
    }

}