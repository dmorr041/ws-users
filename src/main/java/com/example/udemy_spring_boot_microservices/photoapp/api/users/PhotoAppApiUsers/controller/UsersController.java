package com.example.udemy_spring_boot_microservices.photoapp.api.users.PhotoAppApiUsers.controller;

import com.example.udemy_spring_boot_microservices.photoapp.api.users.PhotoAppApiUsers.model.CreateUserRequestModel;
import com.example.udemy_spring_boot_microservices.photoapp.api.users.PhotoAppApiUsers.model.CreateUserResponseModel;
import com.example.udemy_spring_boot_microservices.photoapp.api.users.PhotoAppApiUsers.service.UsersService;
import com.example.udemy_spring_boot_microservices.photoapp.api.users.PhotoAppApiUsers.shared.UserDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    private final Environment env;
    private final UsersService usersService;

    @GetMapping("/status/check")
    public String status() {
        return "Working on port " + env.getProperty("local.server.port");
    }

    @PostMapping(
        consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
        produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDTO userDTO = modelMapper.map(userDetails, UserDTO.class);
        UserDTO createdUser = usersService.createUser(userDTO);

        CreateUserResponseModel returnValue = modelMapper.map(createdUser, CreateUserResponseModel.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }
}
