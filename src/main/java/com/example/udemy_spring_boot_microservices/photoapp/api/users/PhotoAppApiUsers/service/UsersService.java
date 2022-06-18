package com.example.udemy_spring_boot_microservices.photoapp.api.users.PhotoAppApiUsers.service;

import com.example.udemy_spring_boot_microservices.photoapp.api.users.PhotoAppApiUsers.shared.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UsersService extends UserDetailsService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserDetailsByEmail(String email);
}
