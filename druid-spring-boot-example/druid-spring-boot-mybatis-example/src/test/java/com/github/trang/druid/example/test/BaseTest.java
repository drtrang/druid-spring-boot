package com.github.trang.druid.example.test;

import com.github.trang.druid.example.mapper.CityMapper;
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
@SpringBootTest
public abstract class BaseTest {

    @Autowired(required = false)
    protected CityMapper cityMapper;

}