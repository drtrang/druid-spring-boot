package com.github.trang.druid.test;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.trang.druid.autoconfigure.datasource.DruidDataSource2;
import com.github.trang.druid.config.DynamicDataSource;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * 动态数据源测试
 *
 * @author trang
 */
@ActiveProfiles("dynamic")
public class DynamicDataSourceTests extends BaseTest {

    @Autowired
    private DruidDataSource masterDataSource;
    @Autowired
    private DruidDataSource slaveDataSource;
    @Autowired
    private DynamicDataSource dataSource;

    @Test
    public void testMasterDataSource() {
        assertEquals(1, masterDataSource.getInitialSize());
        assertEquals(1, masterDataSource.getMinIdle());
        assertEquals(50, masterDataSource.getMaxActive());
        assertEquals(30000, masterDataSource.getMaxWait());
        assertEquals(60000, masterDataSource.getTimeBetweenEvictionRunsMillis());
        assertEquals(1800000, masterDataSource.getMinEvictableIdleTimeMillis());
        assertEquals(25200000, masterDataSource.getMaxEvictableIdleTimeMillis());
        assertEquals("SELECT 1", masterDataSource.getValidationQuery());
        assertEquals(true, masterDataSource.isTestWhileIdle());
        assertEquals(false, masterDataSource.isTestOnBorrow());
        assertEquals(false, masterDataSource.isTestOnReturn());
        assertEquals(50, masterDataSource.getMaxPoolPreparedStatementPerConnectionSize());
        assertEquals(true, masterDataSource.isUseGlobalDataSourceStat());
    }

    @Test
    public void testSlaveDataSource() {
        assertEquals(1, slaveDataSource.getInitialSize());
        assertEquals(1, slaveDataSource.getMinIdle());
        assertEquals(25, slaveDataSource.getMaxActive());
        assertEquals(30000, slaveDataSource.getMaxWait());
        assertEquals(60000, slaveDataSource.getTimeBetweenEvictionRunsMillis());
        assertEquals(1800000, slaveDataSource.getMinEvictableIdleTimeMillis());
        assertEquals(25200000, slaveDataSource.getMaxEvictableIdleTimeMillis());
        assertEquals("SELECT 1", slaveDataSource.getValidationQuery());
        assertEquals(true, slaveDataSource.isTestWhileIdle());
        assertEquals(false, slaveDataSource.isTestOnBorrow());
        assertEquals(false, slaveDataSource.isTestOnReturn());
        assertEquals(25, slaveDataSource.getMaxPoolPreparedStatementPerConnectionSize());
        assertEquals(true, slaveDataSource.isUseGlobalDataSourceStat());
    }

    @Test
    public void testDynamicDataSource() {
        assertEquals(dataSource.getTargetDataSources().size(), 2);
    }


    @Autowired(required = false)
    private Map<String, DruidDataSource> dataSourceMap;
    @Autowired(required = false)
    private Map<String, DruidDataSource> druidDataSourceMap;
    @Autowired(required = false)
    private Map<String, DruidDataSource> foolDataSourceMap;
    @Autowired(required = false)
    private List<DruidDataSource> dataSourceList;
    @Autowired(required = false)
    private List<DruidDataSource> dataSources;
    @Autowired(required = false)
    private List<DruidDataSource> druidDataSourceList;
    @Autowired(required = false)
    private List<DruidDataSource> druidDataSources;
    @Autowired(required = false)
    private List<DruidDataSource> foolDataSources;
    @Autowired(required = false)
    private Collection<DruidDataSource> druidDataSourceCollection;
    @Autowired(required = false)
    private Set<DruidDataSource> druidDataSourceSet;
    @Autowired(required = false)
    private DruidDataSource[] druidDataSourceArray;


    @Autowired(required = false)
    private Map<String, DruidDataSource2> dataSource2Map;
    @Autowired(required = false)
    private Map<String, DruidDataSource2> druidDataSource2Map;
    @Autowired(required = false)
    private Map<String, DruidDataSource2> foolDataSource2Map;
    @Autowired(required = false)
    private List<DruidDataSource2> dataSource2List;
    @Autowired(required = false)
    private List<DruidDataSource2> dataSources2;
    @Autowired(required = false)
    private List<DruidDataSource2> druidDataSource2List;
    @Autowired(required = false)
    private List<DruidDataSource2> druidDataSources2;
    @Autowired(required = false)
    private List<DruidDataSource2> foolDataSources2;
    @Autowired(required = false)
    private Collection<DruidDataSource2> druidDataSource2Collection;
    @Autowired(required = false)
    private Set<DruidDataSource2> druidDataSource2Set;
    @Autowired(required = false)
    private DruidDataSource2[] druidDataSource2Array;

    @Test
    public void autowire() {
        Assert.assertNotNull(dataSourceMap);
        Assert.assertNotNull(druidDataSourceMap);
        Assert.assertNotNull(foolDataSourceMap);
        Assert.assertNotNull(dataSourceList);
        Assert.assertNotNull(dataSources);
        Assert.assertNotNull(druidDataSourceList);
        Assert.assertNotNull(druidDataSources);
        Assert.assertNotNull(foolDataSources);
        Assert.assertNotNull(druidDataSourceCollection);
        Assert.assertNotNull(druidDataSourceSet);
        Assert.assertNotNull(druidDataSourceArray);

        Assert.assertNotNull(dataSource2Map);
        Assert.assertNotNull(druidDataSource2Map);
        Assert.assertNotNull(foolDataSource2Map);
        Assert.assertNotNull(dataSource2List);
        Assert.assertNotNull(dataSources2);
        Assert.assertNotNull(druidDataSource2List);
        Assert.assertNotNull(druidDataSources2);
        Assert.assertNotNull(foolDataSources2);
        Assert.assertNotNull(druidDataSource2Collection);
        Assert.assertNotNull(druidDataSource2Set);
        Assert.assertNotNull(druidDataSource2Array);
    }

}