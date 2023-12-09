package com.example.testapi.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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
    @NotNull(message = "Username cant be blank!")
    private String username;
    @Email(message = "It has to have the email format!")
    @NotNull(message = "Email cant be blank!")
    private String email;
    @NotNull(message = "Password cant be blank!")
    @Size(min = 9)
    private String password;

    private String dob;

    private String purity;

    private MultipartFile image;

    private String expoPushToken;
}
