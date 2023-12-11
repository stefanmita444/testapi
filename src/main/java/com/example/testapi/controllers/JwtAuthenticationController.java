package com.example.testapi.controllers;

import com.example.testapi.models.JwtRefreshToken;
import com.example.testapi.models.JwtRequest;
import com.example.testapi.models.JwtResponse;
import com.example.testapi.models.SignUpDto;
import com.example.testapi.services.AuthenticationService;
import com.example.testapi.services.JwtTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

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
    public ResponseEntity<JwtResponse> generateAuthenticationToken(
            @Parameter(description = "Authentication request data transfer object", required = true)
            @Valid @RequestBody JwtRequest jwtRequest) throws UsernameNotFoundException {

        log.info("Initiating login--------------------\n");
        JwtResponse jwtResponse = authenticationService.login(jwtRequest);
        log.info("Login success------------------------\n\n");
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    @Operation(
            description = "Register Endpoint",
            summary = "This endpoint will register and create a user and return a JSON WEB Token"
    )
    @PostMapping("/register")
    public ResponseEntity<JwtResponse> registerUser(
            @ModelAttribute SignUpDto signUpDto) throws ParseException {

        log.info("Initiating Registration--------------------------------\n");
        JwtResponse jwtResponse = authenticationService.register(signUpDto);
        log.info("User Registered------------------\n\n");
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<JwtRefreshToken> refreshToken(@RequestBody JwtRefreshToken refreshTokenValue) {
        String refreshToken = jwtTokenService.createToken(refreshTokenValue.getData());

        return ResponseEntity.ok(new JwtRefreshToken(refreshToken));
    }

}
