package com.karhabtiapp.dtos;


import com.karhabtiapp.enums.UserRole;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AuthenticationResponse {

    private String jwt;

    private UserRole userRole;

    private Long userId;



}
