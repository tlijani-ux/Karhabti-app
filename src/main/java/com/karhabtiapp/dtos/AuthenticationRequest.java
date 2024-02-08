package com.karhabtiapp.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AuthenticationRequest {

    private String email;
    private String password;

}
