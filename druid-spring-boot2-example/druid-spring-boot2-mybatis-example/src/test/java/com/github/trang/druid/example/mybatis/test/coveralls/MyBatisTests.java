package com.github.trang.druid.example.mybatis.test.coveralls;

import com.github.trang.druid.example.mybatis.model.City;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

/**
 * MyBatis 测试
 *
 * @author trang
 */
public class MyBatisTests extends BaseTest {

    @Test
    public void testOne() {
        Optional.ofNullable(cityMapper.findById(1L))
                .ifPresent(gson::toJson);
    }

    @Test
    public void testAll() {
        List<City> cities = cityMapper.findAll();
        cities.stream()
                .map(gson::toJson)
                .forEach(log::info);
        Assert.assertEquals(5, cities.size());
    }

}