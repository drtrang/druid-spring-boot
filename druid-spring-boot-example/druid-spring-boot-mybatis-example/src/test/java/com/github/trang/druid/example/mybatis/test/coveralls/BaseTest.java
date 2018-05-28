package com.github.trang.druid.example.mybatis.test.coveralls;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.trang.druid.example.mybatis.DruidMybatisApplication;
import com.github.trang.druid.example.mybatis.mapper.CityMapper;
import com.google.gson.Gson;

/**
 * BaseTest
 *
 * @author trang
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DruidMybatisApplication.class)
public abstract class BaseTest {

    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class.getSimpleName());
    protected Gson gson = new Gson();

    @Autowired(required = false)
    protected CityMapper cityMapper;

}