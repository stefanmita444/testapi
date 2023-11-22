package com.example.testapi.controllers;

import com.example.testapi.exceptions.CustomException;
import com.example.testapi.mappers.UserMapper;
import com.example.testapi.models.*;
import com.example.testapi.services.PushNotificationService;
import com.example.testapi.services.UserService;
import io.github.jav.exposerversdk.PushClientException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/users")
@Tag(name = "User Controller")
//@Api(tags = "User Controller", description = "APIs related to User operations")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final PushNotificationService pushNotificationService;

    @Operation(
            description = "Get endpoint for all users",
            summary = "This endpoint will return a list of users"
    )
    @GetMapping
    public ResponseEntity<UserDtoListDto> getUsers() {
        log.info("Initiating getUsers----------------");
        return new ResponseEntity<>(new UserDtoListDto(userService.findAll().stream()
                .map(UserMapper.INSTANCE::toDto)
                .toList()), HttpStatus.OK);
    }

    @GetMapping("/getMyself")
    public ResponseEntity<UserDto> getMyself() {
        log.info("Getting current user-------------------");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(authentication.getName());
        log.info("Return Complete---------------------\n\n");
        return new ResponseEntity<>( UserMapper.INSTANCE.toDto(user), HttpStatus.OK);
    }

    @Operation(
            description = "Users Search Handle",
            summary = "This endpoint will return a list of users"
    )
    @GetMapping("/search")
    public ResponseEntity<Page<UserDto>> searchUsers(
            @Parameter(description = "Search query")
            @RequestParam("query") String query,
            @Parameter(description = "Page number")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field")
            @RequestParam(defaultValue = "username") String sortBy,
            @Parameter(description = "Sort direction")
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {

        log.info("Fetching all users--------------------------");
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<UserDto> results = userService.searchHandle(query, pageable);
        log.info("Fetching complete--------------------------\n\n");
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @Operation(
            description = "Users Update Endpoint",
            summary = "This endpoint will update a user and will return an updated user object"
    )
    @PatchMapping()
    public ResponseEntity<UserDto> updateUser(
            @ModelAttribute UpdatedUserDTO userDto) {
        log.info("Initiating User Update----------------------------------------------");
        User updatedUser = userService.updateUser(userDto);
        log.info("User Update Complete-------------------------------------\n\n");
        return ResponseEntity.ok(UserMapper.INSTANCE.toDto(updatedUser));
    }

    @Operation(
            description = "Picture Update Endpoint",
            summary = "This endpoint will update a user's picture and will return an updated user object"
    )
    @PostMapping("/picture")
    public ResponseEntity<UserDto> updatePicture(
            @Parameter(description = "Multipart file", required = true)
            @RequestParam("file") MultipartFile file) throws MultipartException {
        try {

            log.info("Initiating picture upload------------------");

            User currentUser = userService.uploadPicture(file);

            log.info("Picture uploaded successfully-----------------------\n\n");

            return ResponseEntity.ok().body(UserMapper.INSTANCE.toDto(currentUser));
        } catch (Exception e) {
            throw new CustomException("Cannot upload picture ");
        }
    }

    @Operation(
            description = "Send Notification Endpoint",
            summary = "This endpoint will send a notification to all the users in a user's friend list and will return a status code 20X"
    )
    @PostMapping("/push")
    public ResponseEntity<HttpStatus> sendPushNotification(

            @Parameter(description = "Push object", required = true)
            @RequestBody Push push) throws PushClientException, InterruptedException {

        log.info("Initiating push--------------------------");

        log.info("Fetching current user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userService.findByUsername(authentication.getName());
        log.info("Current user found");
        log.info("Fetching all friends tokens");
        List<User> friends = pushNotificationService.getFriendsExpoTokens(currentUser.getUsername());
        log.info("Friends tokens found");
        log.info("Sending notification to each friend");
        for (User friend : friends) {
            pushNotificationService.sendPushNotificationToUser(friend.getExpoPushToken(), push.getTitle(), currentUser.getFirstName() + " " + currentUser.getLastName() + " would like for you to pray for them.");
            log.info("Notification sent to friend");
        }
        log.info("Push Complete-----------------------\n\n");
        return ResponseEntity.ok().build();
    }

    @Operation(
            description = "Purity Reset Endpoint",
            summary = "This endpoint will Reset the purity of the current user and return a user object"
    )
    @PostMapping("/resetPurity")
    public ResponseEntity<UserDto> resetPurity() {
        log.info("Initiating Purity Reset-------------------------------");

        User currentUser = userService.resetPurity();

        log.info("Purity Reset Complete---------------------------------\n\n");
        return new ResponseEntity<>(UserMapper.INSTANCE.toDto(currentUser), HttpStatus.OK);
    }

    @Operation(
            description = "Delete User Endpoint",
            summary = "This endpoint will Delete a user from database and all references to the user"
    )
    @DeleteMapping()
    public ResponseEntity<HttpStatus> deleteUser() {
        log.info("Initiating Deletion -----------------------");
        userService.deleteUser();
        log.info("Deletion Complete-----------------------------\n\n");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
