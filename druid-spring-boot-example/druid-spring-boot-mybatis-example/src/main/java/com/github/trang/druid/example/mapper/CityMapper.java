package com.github.trang.druid.example.mapper;

import com.github.trang.druid.example.model.City;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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