package com.example.testapi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.management.InstanceAlreadyExistsException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.testapi.config.JwtTokenUtil;
import com.example.testapi.models.Response;
import com.example.testapi.models.SignUpDto;
import com.example.testapi.models.User;
import com.example.testapi.repos.UserRepo;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

    @Autowired
    JwtTokenUtil tokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    HttpServletRequest request;

    public User findById(String id) {
        Optional<User> user = userRepo.findById(id);

        return user.orElse(null);

    }

    public User createUser(User user) {
        return userRepo.save(user);
    }

    public User findByUsername(String username) {
        Optional<User> user = userRepo.findByUsername(username);
        return user.orElse(null);
    }
    

    public Response register(SignUpDto signUpDto) {

        // add check for username exists in a DB
        if (userRepo.existsByUsername(signUpDto.getUsername())) {
            return new Response("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        // add check for email exists in DB
        if (userRepo.existsByEmail(signUpDto.getEmail())) {
            return new Response("Email is already taken!", HttpStatus.BAD_REQUEST);
        }

        // create user object
        User user = new User();
        user.setFirstName(signUpDto.getFirstName());
        user.setLastName(signUpDto.getLastName());
        user.setUsername(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        user.setFriends(new ArrayList<String>());

        userRepo.save(user);
        return new Response("Registered Successfully!", HttpStatus.OK);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public List<User> findAllFriends(HttpServletRequest request) throws Exception {
        String token = tokenUtil.getToken(request);
        String username = tokenUtil.getUsernameFromToken(token);
        List<User> friends = new ArrayList<>();

        for (String friendId : this.findByUsername(username).getFriends()) {
            friends.add(this.findById(friendId));
        }

        return friends;

    }

    public User addFriend(String friendUsername) {

        String token = tokenUtil.getToken(request);
        String userUsername = tokenUtil.getUsernameFromToken(token);
        User user = this.findByUsername(userUsername);

        if (user.getFriends().contains(friendUsername)) {
            return null;
        }

        user.addFriend(friendUsername);

        return this.createUser(user);
    }

}
