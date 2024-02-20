package com.karhabtiapp.entities;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;

    private String color;

    private String brand;

    private String type;

    private Date modelYear;


    private String transmission;

    private String description;

    private Integer price;


    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;




}
