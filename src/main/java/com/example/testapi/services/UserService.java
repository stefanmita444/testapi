package com.example.testapi.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.testapi.exceptions.CustomException;
import com.example.testapi.exceptions.ResourceNotFoundException;
import com.example.testapi.mappers.UserMapper;
import com.example.testapi.models.FriendDto;
import com.example.testapi.models.HandleInvite;
import com.example.testapi.models.HandledInvite;
import com.example.testapi.models.Invite;
import com.example.testapi.models.InviteDto;
import com.example.testapi.models.MultiDateFormatter;
import com.example.testapi.models.UpdatedUserDTO;
import com.example.testapi.models.User;
import com.example.testapi.models.UserDto;
import com.example.testapi.repos.InvitesRepo;
import com.example.testapi.repos.UserRepo;

import io.github.jav.exposerversdk.PushClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final StorageService storageService;
    private final InvitesRepo invitesRepo;
    private final PushNotificationService notificationService;


    private User currentUser;

    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("Username does not exist!\n\n"));
    }

    public User findById(String id) {
        return userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User with that id does not exist!\n\n"));
    }

    public Page<UserDto> searchHandle(String text, Pageable pageable) {

        log.info("Checking if text is empty");

        if (text == null || text.trim().isEmpty()) {
            log.info("text is empty");
            throw new IllegalArgumentException("Invalid search text\n\n");
        }

        log.info("Getting current user");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        currentUser = findByUsername(authentication.getName());

        log.info("Current user found");
        log.info("Fetching all users from db with that username");

        Page<User> users = userRepo.searchByUsername(text, pageable);

        log.info("Users found");
        log.info("Converting to DTO");

        List<UserDto> userDtoList = users.get()
                .map(UserMapper.INSTANCE::toDto)
                .filter(userDto -> !userDto.getUsername().equalsIgnoreCase(currentUser.getUsername()))
                .collect(Collectors.toList());

        log.info("Conversion done");

        return new PageImpl<>(userDtoList, pageable, userDtoList.size());
    }

    public User uploadPicture(String pictureUrl){

        log.info("Getting current user");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        currentUser = findByUsername(authentication.getName());

        log.info("Current user found");
        log.info("Setting the picture to the user");
        log.info("Checking if picture url is present");

        if (pictureUrl != null) {
            log.info("Picture is not null");
            currentUser.setPicture(pictureUrl);
            log.info("Picture set");
        } else {
            log.error("Picture url is null");
            throw new CustomException("No picture present.\n\n");
        }

        log.info("Saving user in db");
        return userRepo.save(currentUser);
    }

    public void saveInvite(Invite invite) throws PushClientException, InterruptedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        currentUser = findByUsername(authentication.getName());

        User friend = userRepo.findById(invite.getReceiverId()).orElseThrow(() -> new ResourceNotFoundException("No user found with that id\n\n"));
        log.error("Friend found with that id");

        Optional<Invite> existingInvite = invitesRepo.findByRequesterIdAndReceiverId(invite.getReceiverId(), invite.getRequesterId());

        if (existingInvite.isPresent()) {
            return;
        }
        if (currentUser.getFriends().contains(friend.getUsername())) {
            throw new CustomException("Friend already added\n\n");
        }

        if (invite.getRequesterId().equals(currentUser.getId())) {
            log.info("Current user is authorized to send friend request");
            if (invitesRepo.findByRequesterIdAndReceiverId(invite.getRequesterId(), invite.getReceiverId()).isPresent()) {
                throw new CustomException("Invite already sent");
            } else {
                invitesRepo.save(invite);
                log.info("invite saved in db");
                notificationService.sendPushNotificationToUser(friend.getExpoPushToken(), "Friend request", currentUser.getFirstName() + " " + currentUser.getLastName() + " wants to be your friend!");
            }
        } else {
            throw new CustomException("Requester has to be curren user\n\n");
        }
    }

    public List<InviteDto> getRequesterOrReceiverInvites() throws IllegalArgumentException{
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        currentUser = findByUsername(principal);
        log.info("Finding all matching records");

        return invitesRepo.findByRequesterIdOrReceiverId(currentUser.getId(), currentUser.getId()).stream().map(x ->  {
                    User requester = findById(x.getRequesterId());
                    User receiver = findById(x.getReceiverId());
                    return new InviteDto(
                            x.getId(),
                            UserMapper.INSTANCE.toDto(requester),
                            UserMapper.INSTANCE.toDto(receiver)
                    );
                })
                .collect(Collectors.toList());
    }

    public List<InviteDto> getAllInvites () {
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        currentUser = findByUsername(principal);
        return invitesRepo.findAll().stream()
                .flatMap(x -> {
                    try {
                        User requesterUser = findById(x.getRequesterId());
                        User receiverUser = findById(x.getReceiverId());
                        InviteDto inviteDto = new InviteDto(
                                x.getId(),
                                UserMapper.INSTANCE.toDto(requesterUser),
                                UserMapper.INSTANCE.toDto(receiverUser)
                        );
                        return Stream.of(inviteDto);
                    } catch (Exception e) {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toList());
    }

    public List<User> findAll() {
        log.info("Fetching all users");
        return userRepo.findAll();
    }

    public List<UserDto> getFriends() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        currentUser = findByUsername(authentication.getName());
        log.info("Fetching Friends");
        return currentUser.getFriends()
                .stream().
                map(this::findByUsername)
                .map(UserMapper.INSTANCE::toDto)
                .toList();
    }

    public User updateUser(UpdatedUserDTO userDto) throws UsernameNotFoundException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        currentUser = findByUsername(authentication.getName());

        System.out.println(userDto.toString());

        log.info("Starting the Update");

        if (userDto.getFirstName() != null) {
            if (!userDto.getFirstName().equals(currentUser.getFirstName())) {
                if (!userDto.getFirstName().isEmpty()) {
                    log.info("Changing first name");
                    currentUser.setFirstName(userDto.getFirstName());
                    log.info("First name changed");
                } else {
                    log.error("First name is empty");
                }
            } else {
                log.info("First name already exists");
            }
        } else {
            log.info("First name is null");
        }

        if (userDto.getLastName() != null) {
            if (!userDto.getLastName().equals(currentUser.getLastName())) {
                if (!userDto.getLastName().isEmpty()) {
                    log.info("Changing last name");
                    currentUser.setLastName(userDto.getLastName());
                    log.info("Last name changed");
                } else {
                    log.error("Last name is empty");
                }
            } else {
                log.error("Last name not changed, its the same as current user");
            }
        } else {
            log.error("Last name is null");
        }

        if (userDto.getDob() != null) {
            if (!userDto.getDob().equals(currentUser.getDob().toString())) {
                if (!userDto.getLastName().isEmpty()) {
                    log.info("Changing dob");
                    currentUser.setDob(MultiDateFormatter.parseDate(userDto.getDob()));
                    log.info("Dob changed");
                } else {
                        log.error("Dob is empty");
                }
            } else {
                log.info("Dob already exists");
            }
        } else {
            log.info("Dob is null");
        }

        if (userDto.getPurity() != null) {
            if (!userDto.getPurity().equals(currentUser.getPurity().toString())) {
                if (!userDto.getLastName().isEmpty()) {
                    log.info("Changing purity");
                    currentUser.setPurity(MultiDateFormatter.parseDate(userDto.getPurity()));
                    log.info("Purity changed");
                } else {
                    log.error("Purity is empty");
                }
            } else {
                log.info("Purity already exists");
            }
        } else {
            log.info("Purity is null");
        }

        if (userDto.getImageUrl() != null) {
            log.info("Changing picture");
            log.info("Setting picture");
            currentUser.setPicture(userDto.getImageUrl());
            log.info("Picture has been set");
        } else {
            log.info("Picture is null");
        }
        return userRepo.save(currentUser);
    }

    public List<UserDto> deleteFriend(FriendDto friendDto) throws UsernameNotFoundException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        currentUser = findByUsername(authentication.getName());
        User friend = findByUsername(friendDto.getUsername());

        log.info("Initiating Friend Deletion");
        if (findByUsername(friendDto.getUsername()) == null) {
            throw new CustomException("Friend not found\n\n");
        }


        List<String> friends = currentUser.getFriends();
        log.info("Fetching all friends");
        friends.remove(friendDto.getUsername());

        friend.friends.remove(currentUser.getUsername());
        currentUser.setFriends(friends);
        log.info("Friend removed\n\n");
        userRepo.save(friend);
        userRepo.save(currentUser);
        return getFriends();
    }

    public User resetPurity() {
        log.info("Fetching current user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = findByUsername(authentication.getName());
        log.info("Current user found");
        
        currentUser.setPurity(new Date());
        return userRepo.save(currentUser);
    }

    public void deleteUser() {

        log.info("Fetching current user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = findByUsername(authentication.getName());
        log.info("Current user found");

        log.info("Deleting any instance of the user from db");
        for (User userInDB : userRepo.findAll()) {
            if (userInDB.getFriends().contains(currentUser.getUsername())) {
                userInDB.friends.remove(currentUser.getUsername());
                userRepo.save(userInDB);
                log.info("Deleted");
            }
        }
        userRepo.delete(currentUser);
        log.info("User Deleted");

        log.info("Checking if image is present");
        if (currentUser.getPicture() != null) {
            log.info("Image present -> Deleting the image from S3");
            storageService.deleteImage(currentUser.getUsername());
            log.info("Image deleted");
        } else {
            log.info("User has no image");
        }
        log.info("User Deleted");
    }

    public void deleteAllFriends() {
        for (User userInDB : userRepo.findAll()) {
            userInDB.friends = new ArrayList<>();
            userRepo.save(userInDB);
        }
    }

    public void deleteAllInvites() {
        invitesRepo.deleteAll();
    }

    public HandledInvite handleInvite(HandleInvite handleInvite) throws ResourceNotFoundException, PushClientException, InterruptedException {
        String principal = SecurityContextHolder.getContext().getAuthentication().getName();
        currentUser = findByUsername(principal);
        Invite invite = invitesRepo.findById(handleInvite.getId()).orElseThrow(() -> new ResourceNotFoundException("There is no invites with that id\n\n"));
        User friend = userRepo.findById(invite.getRequesterId()).orElseThrow(() -> new ResourceNotFoundException("Theres no friend with that id\n\n"));
        if (invite.getReceiverId().equals(currentUser.getId())) {
            log.error("Current user is the receiver");
            if (handleInvite.getType().equals("accept")) {
                log.error("Friendship Accepted");
                currentUser.addFriend(findById(invite.getRequesterId()).getUsername());
                userRepo.save(currentUser);
                log.error("Current user has added friend");
                friend.addFriend(currentUser.getUsername());
                userRepo.save(friend);
                log.error("Friend has added current user");
                invitesRepo.delete(invite);
            } else if (handleInvite.getType().equals("cancel")){
                invitesRepo.delete(invite);
            } else {
                throw new IllegalArgumentException("Type can be only accept or cancel\n\n");
            }
        } else if (invite.getRequesterId().equals(currentUser.getId())){
            log.info("Requester is current user");
            if (handleInvite.getType().equals("cancel")) {
                invitesRepo.delete(invite);
                log.error("Request canceled");
            } else {
                throw new CustomException("Cannot accept because you are the requester\n\n");
            }
        } else {
            throw new IllegalArgumentException("Must be requester or receiver\n\n");
        }
        return new HandledInvite(currentUser
                .getFriends()
                .stream()
                .map(x -> UserMapper.INSTANCE.toDto(findByUsername(x)))
                .collect(Collectors.toList()), getRequesterOrReceiverInvites());
    }
}