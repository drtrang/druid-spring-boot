package com.github.trang.druid;

import com.github.trang.druid.mapper.CityMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * DruidSpringBootStarterApplication
 *
 * @author trang
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@MapperScan("com.github.trang.druid.mapper")
public class DruidSpringBootStarterApplication implements CommandLineRunner {

    public static void main(String[] args) {
        System.setProperty("druid.logType", "slf4j");
        SpringApplication.run(DruidSpringBootStarterApplication.class, args);
    }

    @Autowired
    private CityMapper cityMapper;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(cityMapper.findAll());
    }

}