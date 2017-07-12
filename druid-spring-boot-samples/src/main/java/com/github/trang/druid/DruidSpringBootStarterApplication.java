package com.github.trang.druid;

import com.github.trang.druid.mapper.CityMapper;
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
public class DruidSpringBootStarterApplication implements CommandLineRunner {

    public static void main(String[] args) {
        System.setProperty("druid.logType", "slf4j");
        SpringApplication.run(DruidSpringBootStarterApplication.class, args);
    }

    @Autowired
    private CityMapper cityMapper;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(cityMapper.findByState("CA"));
    }

}