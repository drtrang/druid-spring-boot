package com.github.trang.druid.example.jpa.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * City
 *
 * @author trang
 */
@Entity
@Table(name = "city")
@Data
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

}