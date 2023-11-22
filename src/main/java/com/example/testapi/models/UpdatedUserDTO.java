package com.example.testapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedUserDTO {

    private String firstName;
    private String lastName;
    private MultipartFile image;
    private String dob;
    private String purity;
}
