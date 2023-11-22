package com.example.testapi.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@ApiModel(description = "Details about the friend")
public class FriendDto {
    @NotNull
    String username;
}
