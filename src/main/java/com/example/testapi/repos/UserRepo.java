package com.example.testapi.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.testapi.models.User;

public interface UserRepo extends MongoRepository<User, String> {
    List<User> findAll();
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

}
