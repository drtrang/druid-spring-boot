package com.github.trang.druid.example.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.trang.druid.example.jpa.model.City;

/**
 * CityRepository
 *
 * @author trang
 */
public interface CityRepository extends JpaRepository<City, Long> {

}