package com.github.trang.druid.test;

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
@ActiveProfiles("dev-yaml")
//@ActiveProfiles("dev-props")
public class DevProfilesTests extends BaseTest {

    @Autowired
    private DruidDataSource dataSource;

    @Test
    public void testSingleDataSource() {
        assertEquals(100000, dataSource.getMinEvictableIdleTimeMillis());
        assertEquals(200000, dataSource.getMaxEvictableIdleTimeMillis());
    }

}