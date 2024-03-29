package com.example.testapi.controllers;

import com.example.testapi.models.*;
import com.example.testapi.services.UserService;
import io.github.jav.exposerversdk.PushClientException;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("api/v1/invites")
@Tag(name = "Invites Controller")
@RequiredArgsConstructor
public class InvitesController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ResponseWrapper<List<InviteDto>>> friendRequest(
            @Parameter(description = "Invite object", required = true)
            @RequestBody Invite invite) throws PushClientException, InterruptedException {
        log.info("Initiating invitation");
        userService.saveInvite(invite);
        log.info("Invite sent");
        return new ResponseEntity<>(new ResponseWrapper<>(userService.getRequesterOrReceiverInvites()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseWrapper<List<InviteDto>>> getRequesterOrReceiverInvites() throws IllegalArgumentException {
        log.info("Initiating getting MATCHING Invites---------------------------");

        List<InviteDto> invitesDTO = userService.getRequesterOrReceiverInvites();

        log.info("Getting matching invites complete--------------------------\n\n");

        return new ResponseEntity<>(new ResponseWrapper<>(invitesDTO), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseWrapper<List<InviteDto>>> getAllInvites() {
        log.info("Initiating getting ALL Invites---------------------------");

        List<InviteDto> invitesDTO = userService.getAllInvites();

        log.info("Getting all invites complete--------------------------\n\n");
        return new ResponseEntity<>(new ResponseWrapper<>(invitesDTO), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<ResponseWrapper<HttpStatus>> deleteAllInvites() {

        log.info("Initiating Deleting All Invites---------------------------");

        userService.deleteAllInvites();

        log.info("Deleting all invites complete--------------------------\n\n");
        return new ResponseEntity<>(new ResponseWrapper<>(HttpStatus.OK), HttpStatus.OK);
    }

    @PostMapping("/handle")
    public ResponseEntity<ResponseWrapper<HandledInvite>> handleInvite(
            @RequestBody HandleInvite handleInvite) throws PushClientException, InterruptedException {
        log.info("Initiating handling---------------------------");
        HandledInvite handledInvite = userService.handleInvite(handleInvite);
        log.info("Handling Complete---------------------------\n\n");
        return new ResponseEntity<>(new ResponseWrapper<>(handledInvite), HttpStatus.OK);
    }
}
