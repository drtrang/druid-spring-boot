package com.github.trang.druid.example.mybatis.test.coveralls;

import com.github.trang.druid.example.mybatis.model.City;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * MyBatis 测试
 *
 * @author trang
 */
public class MyBatisTests extends BaseTest {

    @Test
    public void testOne() {
        City city = cityMapper.findById(1L);
        System.out.println(city);
    }

    @Test
    public void testAll() {
        List<City> cities = cityMapper.findAll();
        System.out.println(cities);
        Assert.assertEquals(5, cities.size());
    }

}