package com.example.testapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InviteDto {
    private String id;
    private UserDto requesterUser;
    private UserDto receiverUser;
}
