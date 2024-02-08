package com.karhabtiapp.dtos;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Getter
@Setter
public class SignupRequest {

    private String email;

    private  String name;

    private String password;




}
