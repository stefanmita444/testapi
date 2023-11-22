package com.example.testapi.controllers;

import com.example.testapi.models.FriendDto;
import com.example.testapi.models.FriendsCollectionDto;
import com.example.testapi.models.UserDto;
import com.example.testapi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/friends")
//@Api(value = "Friend Controller", description = "APIs related to Friend operations")
@Tag(name = "Friend Controller")
@RequiredArgsConstructor
public class FriendController {

    private final UserService userService;

    @Operation(
            description = "Get Friends Endpoint",
            summary = "This endpoint will return all friends of a user"
    )
    @GetMapping()
    public ResponseEntity<FriendsCollectionDto> getFriends() {
        log.info("Fetching all friends------------------------");

        List<UserDto> friends = userService.getFriends();

        log.info("Friends found--------------------------\n\n");

        return ResponseEntity.ok(new FriendsCollectionDto(friends));
    }

    @Operation(
            description = "Remove Friend Endpoint",
            summary = "This endpoint will remove a friend from the friend list and return an updated user object"
    )
    @PostMapping("/remove")
    public ResponseEntity<FriendsCollectionDto> removeFriend(
      //      @ApiParam(value = "Friend data transfer object", required = true)
            @RequestBody FriendDto friendDto) {
        log.info("Initiating Friend Removal-----------------------");
        List<UserDto> friends = userService.deleteFriend(friendDto);
        log.info("Friend Removal Compete--------------------------\n\n");
        return new ResponseEntity<>(new FriendsCollectionDto(friends), HttpStatus.OK);
    }

    @PostMapping("/deleteAll")
    public void deleteAllFriends() {
        log.info("Initiating ALL Friends Deletion---------------------");
        userService.deleteAllFriends();
        log.info("Friend Deletion Complete-------------------\n\n");
    }

}