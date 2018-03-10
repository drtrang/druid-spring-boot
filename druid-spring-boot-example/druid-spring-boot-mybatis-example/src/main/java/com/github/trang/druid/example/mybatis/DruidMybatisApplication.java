package com.github.trang.druid.example.mybatis;

import com.github.trang.druid.example.mybatis.mapper.CityMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * DruidSpringBootStarterApplication
 *
 * @author trang
 */
@SpringBootApplication
@MapperScan("com.github.trang.druid.example.mapper")
@Slf4j
public class DruidMybatisApplication implements CommandLineRunner {

    public static void main(String[] args) {
        System.setProperty("druid.logType", "slf4j");
        SpringApplication.run(DruidMybatisApplication.class, args);
    }

    @Autowired
    private CityMapper cityRepository;

    @Override
    public void run(String... args) {
        Gson gson = new Gson();
        cityRepository.findAll().stream()
                .map(gson::toJson)
                .forEach(log::info);
    }

}