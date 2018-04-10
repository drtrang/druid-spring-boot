package com.github.trang.druid.example.jpa.repository;

import com.github.trang.druid.example.jpa.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CityRepository
 *
 * @author trang
 */
public interface CityRepository extends JpaRepository<City, Long> {

}