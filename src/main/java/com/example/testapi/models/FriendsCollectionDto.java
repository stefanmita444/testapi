package com.example.testapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
//@ApiModel(description = "List of friends")
public class FriendsCollectionDto {
    List<UserDto> friends = new ArrayList<>();
}
