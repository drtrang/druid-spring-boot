package com.github.trang.druid.example.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.trang.druid.example.jpa.repository.CityRepository;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

/**
 * DruidSpringBootStarterApplication
 *
 * @author trang
 */
@SpringBootApplication
@Slf4j
public class DruidJpaApplication implements CommandLineRunner {

    public static void main(String[] args) {
        System.setProperty("druid.logType", "slf4j");
        SpringApplication.run(DruidJpaApplication.class, args);
    }

    @Autowired
    private CityRepository cityRepository;

    @Override
    public void run(String... args) {
        Gson gson = new Gson();
        cityRepository.findAll().stream()
                .map(gson::toJson)
                .forEach(log::info);
    }

}