package com.github.trang.druid.example.mybatis.test.coveralls;

import com.github.trang.druid.example.mybatis.DruidMybatisApplication;
import com.github.trang.druid.example.mybatis.mapper.CityMapper;
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
@SpringBootTest(classes = DruidMybatisApplication.class)
public abstract class BaseTest {

    @Autowired(required = false)
    protected CityMapper cityMapper;

}