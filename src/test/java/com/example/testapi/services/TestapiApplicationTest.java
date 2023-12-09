package com.example.testapi.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.testapi.config.CustomAuthenticationManager;
import com.example.testapi.config.JwtTokenUtil;
import com.example.testapi.exceptions.CustomException;
import com.example.testapi.models.JwtResponse;
import com.example.testapi.models.SignUpDto;
import com.example.testapi.models.User;
import com.example.testapi.repos.UserRepo;

@SpringBootTest
public class TestapiApplicationTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock 
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private StorageService storageService;

    @Mock
    private CustomAuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    private SignUpDto signUpDto;

    @BeforeEach
    void setup() {
        signUpDto = SignUpDto.builder()
                        .firstName("Mike")
                        .lastName("Kowalski")
                        .username("stefanmita123")
                        .email("stefanmita444@gmail.com")
                        .password("somePass")
                        .build();

        when(userRepo.existsByUsername(signUpDto.getUsername())).thenReturn(false);
        when(userRepo.existsByEmail(signUpDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(signUpDto.getPassword())).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(jwtTokenUtil.generateToken(any(User.class))).thenReturn("token");
    }

    @Test
    void testRegistration() throws UsernameNotFoundException, CustomException, ParseException {
        
        JwtResponse jwtResponse = authenticationService.register(signUpDto);

        assertNotNull(jwtResponse);
        assertEquals("token", jwtResponse.getJwtToken());
        verify(userRepo).save(any(User.class));
        
    }

}
