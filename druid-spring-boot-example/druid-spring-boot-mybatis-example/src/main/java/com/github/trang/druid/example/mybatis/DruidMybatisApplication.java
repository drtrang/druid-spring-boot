package com.github.trang.druid.example.mybatis;

import org.mybatis.spring.annotation.MapperScan;
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
public class DruidMybatisApplication implements CommandLineRunner {

    public static void main(String[] args) {
        System.setProperty("druid.logType", "slf4j");
        SpringApplication.run(DruidMybatisApplication.class, args);
    }

    @Override
    public void run(String... args) {

    }

}