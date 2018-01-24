package com.github.trang.druid.example.jpa.test.coveralls;

import com.github.trang.druid.example.jpa.DruidSpringBootStarterApplication;
import com.github.trang.druid.example.jpa.repository.CityRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * BaseTest
 *
 * @author trang
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DruidSpringBootStarterApplication.class)
public abstract class BaseTest {

    @Autowired(required = false)
    protected CityRepository cityRepository;

}