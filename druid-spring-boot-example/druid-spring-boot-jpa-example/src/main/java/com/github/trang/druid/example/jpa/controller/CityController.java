package com.github.trang.druid.example.jpa.controller;

import com.github.trang.druid.example.jpa.model.City;
import com.github.trang.druid.example.jpa.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CityController
 *
 * @author trang
 */
@RestController
@RequestMapping("/city")
public class CityController {

    @Autowired
    private CityRepository cityRepository;

    @GetMapping("/get/{id}")
    public City get(@PathVariable Long id) {
        return cityRepository.findOne(id);
    }

}