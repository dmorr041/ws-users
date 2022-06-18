package com.example.udemy_spring_boot_microservices.photoapp.api.users.PhotoAppApiUsers.shared;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 6543924796336072780L;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String userID;
    private String encryptedPassword;
}
