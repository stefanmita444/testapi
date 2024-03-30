package com.example.testapi.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {
    
    @NotNull(message = "Username/email cannot be empty!")
    private String principal;

    @NotNull(message = "Password cannot be empty!")
    @Size(min = 9, message = "Password must be greater than 9 characters!")
    private String password;

    private String expoPushToken;
}