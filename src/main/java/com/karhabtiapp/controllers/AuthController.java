package com.karhabtiapp.controllers;

import com.karhabtiapp.dtos.AuthenticationRequest;
import com.karhabtiapp.dtos.AuthenticationResponse;
import com.karhabtiapp.dtos.SignupRequest;
import com.karhabtiapp.dtos.UserDto;
import com.karhabtiapp.entities.User;
import com.karhabtiapp.enums.UserRole;
import com.karhabtiapp.repositories.UserRepository;
import com.karhabtiapp.services.AuthService;
import com.karhabtiapp.services.jwt.UserService;
import com.karhabtiapp.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
      AuthenticationManager authenticationManager;

    @Autowired
    JwtUtil jwtUtil;


    @Autowired
    UserService userServices;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> createCustomer(@RequestBody SignupRequest signupRequest) {
        if (authService.hasCustomerWithEmail(signupRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);

        }
        UserDto createdUserDto = authService.createCustomer(signupRequest);
        if (createdUserDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }



    @PostMapping(value = "/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest authenticationRequest) {
        System.out.println(authenticationRequest.getEmail());
        System.out.println(authenticationRequest.getPassword());
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));
        // Retrieve UserDetails object
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        final String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }


}