package com.github.trang.druid.example.jpa.test.coveralls;

import com.alibaba.druid.pool.DruidDataSource;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.Assert.assertEquals;

/**
 * 单数据源测试
 *
 * @author trang
 */
@ActiveProfiles("default")
public class SingleDataSourceTests extends BaseTest {

    @Autowired
    private DruidDataSource dataSource;

    @Test
    public void testSingleDataSource() {
        assertEquals(1, dataSource.getInitialSize());
        assertEquals(1, dataSource.getMinIdle());
        assertEquals(10, dataSource.getMaxActive());
        assertEquals(30000, dataSource.getMaxWait());
        assertEquals(60000, dataSource.getTimeBetweenEvictionRunsMillis());
        assertEquals(1800000, dataSource.getMinEvictableIdleTimeMillis());
        assertEquals(25200000, dataSource.getMaxEvictableIdleTimeMillis());
        assertEquals("SELECT 1", dataSource.getValidationQuery());
        assertEquals(true, dataSource.isTestWhileIdle());
        assertEquals(false, dataSource.isTestOnBorrow());
        assertEquals(false, dataSource.isTestOnReturn());
        assertEquals(20, dataSource.getMaxPoolPreparedStatementPerConnectionSize());
        assertEquals(true, dataSource.isUseGlobalDataSourceStat());
    }

}