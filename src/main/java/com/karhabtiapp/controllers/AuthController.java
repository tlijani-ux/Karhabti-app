package com.karhabtiapp.controllers;

import com.karhabtiapp.dtos.AuthenticationRequest;
import com.karhabtiapp.dtos.AuthenticationResponse;
import com.karhabtiapp.dtos.SignupRequest;
import com.karhabtiapp.dtos.UserDto;
import com.karhabtiapp.entities.User;
import com.karhabtiapp.repositories.UserRepository;
import com.karhabtiapp.services.AuthService;
import com.karhabtiapp.services.jwt.UserService;
import com.karhabtiapp.utils.JwtUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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

        
        
        @PostMapping("/login")
    public AuthenticationResponse createdAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws BadCredentialsException,
            DisabledException,
            UsernameNotFoundException{

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        }catch (BadCredentialsException e){
            throw  new BadCredentialsException("incorect username or password");
        }

        final UserDetails userDetails = userServices.DetailsService().loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());

        String jwt = jwtUtil.generateToken(userDetails);

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        if (optionalUser.isPresent()){

            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserId(optionalUser.get().getId());
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
        }
        return authenticationResponse;
    }

}
