package com.example.testapi.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedUserDTO {

    private String firstName;
    private String lastName;
    private String imageUrl;
    private String dob;
    private String purity;
}
