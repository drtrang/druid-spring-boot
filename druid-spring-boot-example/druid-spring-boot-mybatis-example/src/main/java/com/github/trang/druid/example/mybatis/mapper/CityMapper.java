package com.github.trang.druid.example.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.github.trang.druid.example.mybatis.model.City;

/**
 * CityMapper
 *
 * @author trang
 */
@Mapper
public interface CityMapper {

    @Select("select * from city")
    List<City> findAll();

    @Select("select * from city where id = #{id}")
    City findById(@Param("id") Long id);

}