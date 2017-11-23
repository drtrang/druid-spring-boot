package com.github.trang.druid.example.controller;

import com.github.trang.druid.example.mapper.CityMapper;
import com.github.trang.druid.example.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * CityController
 *
 * @author trang
 */
@RestController
@RequestMapping("/city")
public class CityController {

    @Autowired
    private CityMapper cityMapper;

    @GetMapping("/get/{id}")
    public City get(@PathVariable Long id) {
        return cityMapper.findById(id);
    }

}