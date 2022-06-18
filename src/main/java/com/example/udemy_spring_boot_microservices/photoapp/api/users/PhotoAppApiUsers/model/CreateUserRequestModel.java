package com.example.udemy_spring_boot_microservices.photoapp.api.users.PhotoAppApiUsers.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateUserRequestModel {

    @NotNull(message = "First name is required")
    @Size(min=2, message = "First name must be at least 2 characters")
    private String firstName;

    @NotNull(message = "Last name is required")
    @Size(min=2, message = "Last name must be at least 2 characters")
    private String lastName;

    @NotNull(message = "Email is required")
    @Email
    private String email;

    @NotNull(message = "Password is required")
    @Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters")
    private String password;
}
