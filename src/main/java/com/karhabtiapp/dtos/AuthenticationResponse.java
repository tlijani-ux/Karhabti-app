package com.karhabtiapp.dtos;


import com.karhabtiapp.enums.UserRole;
import lombok.*;

@Data
@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class AuthenticationResponse {

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }
    public AuthenticationResponse(String token, UserRole userRole, Long userId) {
        this.jwt = token;
        this.userRole = userRole;
        this.userId = userId;
    }


    private String jwt;

    private UserRole userRole;


    public Long userId;


}
