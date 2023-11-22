package com.example.testapi.services;

import com.example.testapi.config.JwtTokenUtil;
import com.example.testapi.exceptions.CustomException;
import com.example.testapi.models.User;
import com.example.testapi.repos.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtTokenService {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepo userRepo;

    public String createToken(String token) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepo.findByUsername(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        token = token.substring(7);
        if (validate(token)) {
            return jwtTokenUtil.generateToken(currentUser);
        } else {
            throw new CustomException("Token is invalid or expired");
        }
    }

    public boolean validate(String token) {
        // logic to validate JWT token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userRepo.findById(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return jwtTokenUtil.validateToken(token, currentUser);
    }
}

