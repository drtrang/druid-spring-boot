package com.github.trang.druid.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallFilter;
import com.github.trang.druid.mapper.CityMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author trang
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class DruidTests {

    @Autowired
    private DruidDataSource dataSource;
    @Autowired
    private WallFilter wallFilter;
    @Autowired
    private CityMapper cityMapper;

    @Test
    public void testDataSource() {
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

    @Test
    public void testMybatis() {
        System.out.println(cityMapper.findById(1L));
    }

}