package com.example.testapi.controllers;

import java.text.ParseException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.testapi.models.JwtRefreshToken;
import com.example.testapi.models.JwtRequest;
import com.example.testapi.models.ResponseWrapper;
import com.example.testapi.models.SignUpDto;
import com.example.testapi.services.AuthenticationService;
import com.example.testapi.services.JwtTokenService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Auth Controller")
public class JwtAuthenticationController {

    private final AuthenticationService authenticationService;
    private final JwtTokenService jwtTokenService;

    @Operation(
            description = "Login Endpoint",
            summary = "This endpoint will log in a user and return a JSON WEB Token"
    )
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<ResponseWrapper<String>> generateAuthenticationToken(
            @Parameter(description = "Authentication request data transfer object", required = true)
            @Valid @RequestBody JwtRequest jwtRequest) throws UsernameNotFoundException {

        log.info("Initiating login--------------------\n");
        String jwtResponse = authenticationService.login(jwtRequest);
        log.info("Login success------------------------\n\n");
        return new ResponseEntity<>(new ResponseWrapper<>(jwtResponse), HttpStatus.OK);
    }

    @Operation(
            description = "Register Endpoint",
            summary = "This endpoint will register and create a user and return a JSON WEB Token"
    )
    @PostMapping("/register")
    public ResponseEntity<ResponseWrapper<String>> registerUser(
            @ModelAttribute SignUpDto signUpDto) throws ParseException {

        log.info("Initiating Registration--------------------------------\n");
        String jwtResponse = authenticationService.register(signUpDto);
        log.info("User Registered------------------\n\n");
        return new ResponseEntity<>(new ResponseWrapper<>(jwtResponse), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseWrapper<String>> refreshToken(@RequestBody JwtRefreshToken refreshTokenValue) {
        String refreshToken = jwtTokenService.createToken(refreshTokenValue.getData());

        return ResponseEntity.ok(new ResponseWrapper<>(refreshToken));
    }

}
