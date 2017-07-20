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
@ActiveProfiles("dynamic-dev")
public class DynamicDevProfilesTests extends BaseTest {

    @Autowired
    private DruidDataSource firstDataSource;
    @Autowired
    private DruidDataSource secondDataSource;
    @Autowired
    private DynamicDataSource dataSource;

    @Test
    public void testFirstDataSource() {
        assertEquals(100000, firstDataSource.getMinEvictableIdleTimeMillis());
        assertEquals(200000, firstDataSource.getMaxEvictableIdleTimeMillis());
    }

    @Test
    public void testSecondDataSource() {
        assertEquals(100000, secondDataSource.getMinEvictableIdleTimeMillis());
        assertEquals(200000, secondDataSource.getMaxEvictableIdleTimeMillis());
    }

}