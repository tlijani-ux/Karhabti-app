package com.karhabtiapp.services;


import com.karhabtiapp.dtos.SignupRequest;
import com.karhabtiapp.dtos.UserDto;
import com.karhabtiapp.entities.User;
import com.karhabtiapp.enums.UserRole;
import com.karhabtiapp.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {



    @Autowired
    private  UserRepository userRepository;


    @PostConstruct
    public void createAdminAccount() {
       User Admin = userRepository.findByUserRole(UserRole.ADMIN);
       if (Admin==null){
           User newAdmin = new User();
           newAdmin.setName("ADMIN");
           newAdmin.setUserRole(UserRole.ADMIN);
           newAdmin.setEmail("admin@gmail.com");
           newAdmin.setPassword(new BCryptPasswordEncoder().encode("admin"));
           userRepository.save(newAdmin);
       }
    }



    @Override
    public UserDto createCustomer(@NotNull SignupRequest signupRequest) {

        if (signupRequest == null) {
            throw new IllegalArgumentException("signupRequest must not be null");
        }

        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setName(signupRequest.getName());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setUserRole(UserRole.CUSTOMER);
        User createdCustomer = userRepository.save(user);

        UserDto createdUserDto = new UserDto();
        createdUserDto.setId(createdCustomer.getId());
        createdUserDto.setName(createdCustomer.getName());
        createdUserDto.setEmail(createdCustomer.getEmail());
        createdUserDto.setUserRole(createdCustomer.getUserRole());

        return createdUserDto;
    }

    @Override
    public boolean hasCustomerWithEmail(String email) {
      return userRepository.findFirstByEmail(email).isPresent();


    }
}
