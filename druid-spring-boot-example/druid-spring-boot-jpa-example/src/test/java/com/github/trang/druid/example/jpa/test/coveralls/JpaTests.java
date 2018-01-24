package com.github.trang.druid.example.jpa.test.coveralls;

import com.github.trang.druid.example.jpa.model.City;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * MyBatis 测试
 *
 * @author trang
 */
public class JpaTests extends BaseTest {

    @Test
    public void testOne() {
        City city = cityRepository.findOne(1L);
        System.out.println(city);
    }

    @Test
    public void testAll() {
        List<City> cities = cityRepository.findAll();
        System.out.println(cities);
        Assert.assertEquals(5, cities.size());
    }

}