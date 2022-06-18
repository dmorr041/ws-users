package com.example.udemy_spring_boot_microservices.photoapp.api.users.PhotoAppApiUsers.service;

import com.example.udemy_spring_boot_microservices.photoapp.api.users.PhotoAppApiUsers.data.UserEntity;
import com.example.udemy_spring_boot_microservices.photoapp.api.users.PhotoAppApiUsers.data.UsersRepository;
import com.example.udemy_spring_boot_microservices.photoapp.api.users.PhotoAppApiUsers.shared.UserDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        userDTO.setUserID(UUID.randomUUID().toString());
        userDTO.setEncryptedPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserEntity userEntity = mapper.map(userDTO, UserEntity.class);

        usersRepository.save(userEntity);

        return mapper.map(userEntity, UserDTO.class);
    }

    @Override
    public UserDTO getUserDetailsByEmail(String email) {
        UserEntity userEntity = usersRepository.findByEmail(email);

        if (userEntity == null) throw new UsernameNotFoundException(email);

        return new ModelMapper().map(userEntity, UserDTO.class);
    }

    /**
     * Pulls a user from the DB via JPA enabled findByEmail method we have implemented in our UsersRepository.
     * @param username The email which is trying to authenticate
     * @return a spring security User instance
     * @throws UsernameNotFoundException - spring security built in exception
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = usersRepository.findByEmail(username);

        if (userEntity == null) throw new UsernameNotFoundException(username);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
    }


}
