package com.example.testapi.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@ApiModel(description = "Register Format")
public class SignUpDto {

    @NotNull(message = "First name can't be blank!")
    private String firstName;
    
    @NotNull(message = "Last name cant be blank!")
    private String lastName;

    @NotNull(message = "Username can't be blank")
    @Size(min = 8, max = 20, message = "Username must be greater than 8 characters")
    private String username;

    @NotNull(message = "Email cant be null")
    @Email(message = "Email must follow email formatting")
    private String email;

    @NotNull(message = "Password cant be blank")
    @Size(min = 9, message = "Password must be greater than 9 characters")
    private String password;

    private String dob;

    private String purity;

    private String image;

    private String expoPushToken;
}
