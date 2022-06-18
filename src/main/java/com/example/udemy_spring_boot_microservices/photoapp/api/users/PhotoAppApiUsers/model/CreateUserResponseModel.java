package com.example.udemy_spring_boot_microservices.photoapp.api.users.PhotoAppApiUsers.model;

import lombok.Data;

@Data
public class CreateUserResponseModel {
    private String firstName;
    private String lastName;
    private String email;
    private String userID;
}
