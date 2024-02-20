package com.karhabtiapp.dtos;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class CarDto {


    private Long id;


    private String name;

    private String color;

    private String transmission;


    private String brand;

    private String type;

    private Date modelYear;



    private String description;

    private Integer price;

    private MultipartFile image;


    private byte[] returnedImage;






}
