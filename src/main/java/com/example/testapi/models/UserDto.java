package com.example.testapi.models;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
//@ApiModel(description = "Details about the user")
public class UserDto {

    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String picture;
    private Date dob;
    private Date purity;
    private List<String> friends;
    private String expoPushToken;
    
    public UserDto(String id, String firstName, String lastName, String username, String email, String picture, Date dob, Date purity, List<String> friends, String expoPushToken) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.picture = picture;
        this.dob = dob;
        this.purity = purity;
        this.friends = friends;
        this.expoPushToken = expoPushToken;
    }

}
