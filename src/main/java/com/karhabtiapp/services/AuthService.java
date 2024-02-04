package com.karhabtiapp.services;

import com.karhabtiapp.dtos.SignupRequest;
import com.karhabtiapp.dtos.UserDto;

public interface AuthService {

    UserDto createCustomer(SignupRequest signupRequest);

    boolean hasCustomerWithEmail(String email);
}
