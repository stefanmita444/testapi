package com.example.testapi.models;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "invites")
//@ApiModel(description = "Invitation Object")
public class Invite {
    @Id
    private String id;
    @NotNull(message = "requester cant be null")
    private String requesterId;
    private String receiverId;
}
