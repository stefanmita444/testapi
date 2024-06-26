package com.example.testapi.services;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.testapi.config.CustomAuthenticationManager;
import com.example.testapi.config.JwtTokenUtil;
import com.example.testapi.exceptions.CustomException;
import com.example.testapi.exceptions.ResourceNotFoundException;
import com.example.testapi.models.JwtRequest;
import com.example.testapi.models.MultiDateFormatter;
import com.example.testapi.models.SignUpDto;
import com.example.testapi.models.User;
import com.example.testapi.repos.UserRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomAuthenticationManager authenticationManager;


    public String register(SignUpDto signUpDto) throws CustomException, ParseException, UsernameNotFoundException {

        signUpDto.setEmail(signUpDto.getEmail().toLowerCase());
        signUpDto.setUsername(signUpDto.getUsername().toLowerCase());
        log.info("Checking if username exists");
        if (userRepo.existsByUsername(signUpDto.getUsername())) {
            throw new CustomException("Username taken\n\n");
        }

        log.info(signUpDto.toString());

        log.info("Checking if email exists");
        if (userRepo.existsByEmail(signUpDto.getEmail())) {
            throw new CustomException("Email taken\n\n");
        }

        log.info("Creating user");
        User currentUser = User.builder()
                .firstName(signUpDto.getFirstName())
                .lastName(signUpDto.getLastName())
                .username(signUpDto.getUsername())
                .email(signUpDto.getEmail())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .purity(new Date())
                .friends(new ArrayList<>())
                .picture(signUpDto.getImage())
                .expoPushToken(signUpDto.getExpoPushToken())
                .build();

        if (signUpDto.getDob() != null) {
            log.info("Dob: " + signUpDto.getDob());
            currentUser.setDob(MultiDateFormatter.parseDate(signUpDto.getDob()));
        }

        log.info("User Created");

        log.info("Saving user in db");
        userRepo.save(currentUser);
        log.info("User saved in db");
        log.info("Generating token");
        String token = jwtTokenUtil.generateToken(currentUser);
        log.info("Token generated");
        return token;
    }

    private void authenticate(JwtRequest jwtRequest) throws AuthenticationException {
        String principal = jwtRequest.getPrincipal();
        String credentials = jwtRequest.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(principal, credentials));
    }


    public String login (JwtRequest jwtRequest) throws CustomException{
        log.info("Converting principal to lowercase");
        jwtRequest.setPrincipal(jwtRequest.getPrincipal().toLowerCase());
        log.info("Done");
        log.info("Authenticating");
        authenticate(jwtRequest);
        log.info("Done");
        log.info("Getting user");
        User currentUser = userRepo.findByUsernameOrEmail(jwtRequest.getPrincipal(), jwtRequest.getPrincipal()).orElseThrow(() -> new ResourceNotFoundException("Could not find user with that login/email\n\n"));
        log.info("Setting the expoPushToken");
        currentUser.setExpoPushToken(jwtRequest.getExpoPushToken());
        log.info("Saving user in db");
        userRepo.save(currentUser);
        log.info("Done");
        return jwtTokenUtil.generateToken(currentUser);
    }

}
