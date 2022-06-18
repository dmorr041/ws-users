package com.example.udemy_spring_boot_microservices.photoapp.api.users.PhotoAppApiUsers.model;

import lombok.Data;

@Data
public class LoginRequestModel {
    private String email;
    private String password;
}
