package com.example.testapi.controllers;

import java.util.List;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.testapi.models.EzerException;
import com.example.testapi.models.User;
import com.example.testapi.services.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/users")
@PreAuthorize("isAuthenticated()")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getContractor() {
        List<User> contractors = userService.findAll();
        return ResponseEntity.ok(contractors);
    }

    @PostMapping
    public ResponseEntity<User> createContractor(@RequestBody User contractor) {
        User newContractor = userService.createUser(contractor);
        return ResponseEntity.ok(newContractor);
    }

    @GetMapping("/friends")
    public ResponseEntity<List<User>> getFriends(HttpServletRequest request) throws Exception {
        
        return new ResponseEntity<>(userService.findAllFriends(request), HttpStatus.OK);

    }

    @PostMapping("/friends")
    public ResponseEntity<?> addFriend(@RequestBody String friendUsername) {
        User friendCreated = userService.addFriend(friendUsername);
        if (friendCreated != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
    }


}
