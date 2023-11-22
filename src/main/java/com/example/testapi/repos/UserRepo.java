package com.example.testapi.repos;

import com.example.testapi.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameOrEmail(String username, String email);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    Optional<List<User>> findAllByUsername(String username);

    @Query("{ 'username': { '$regex': ?0, '$options': 'i' } }")
    Page<User> searchByUsername(String query, Pageable pageable);


}
