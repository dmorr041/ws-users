package com.example.udemy_spring_boot_microservices.photoapp.api.users.PhotoAppApiUsers.data;

import org.springframework.data.repository.CrudRepository;

/**
 * For any repository in Spring JPA, the way you name the database update methods (find, update, delete), matters!
 * findBy - When I want to SELECT something
 * delete - When I want to DELETE something
 * update - When I want to CREATE something
 * What comes after that must be a field in the entity we are storing. I.e. in this case, UserEntity must have a field
 * called "email".
 */
public interface UsersRepository extends CrudRepository<UserEntity, Long> {   // <Java Class we are storing, Type of the ID for each entity>
    UserEntity findByEmail(String email);
}
