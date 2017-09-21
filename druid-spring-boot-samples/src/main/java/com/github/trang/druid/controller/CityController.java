package com.github.trang.druid.controller;

import com.github.trang.druid.mapper.CityMapper;
import com.github.trang.druid.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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