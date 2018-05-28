package com.github.trang.druid.example.mybatis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.trang.druid.example.mybatis.mapper.CityMapper;
import com.github.trang.druid.example.mybatis.model.City;

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