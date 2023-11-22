package com.example.testapi.services;

import com.example.testapi.exceptions.CustomException;
import com.example.testapi.exceptions.ResourceNotFoundException;
import com.example.testapi.models.User;
import com.example.testapi.repos.UserRepo;
import io.github.jav.exposerversdk.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushNotificationService {
    private final UserRepo userRepo;

    private final PushClient pushClient;


    public List<User> getFriendsExpoTokens(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User with ID: " + username + " does not exist\n\n"));

        List<String> friendsUsernames = user.getFriends();
        List<User> friends = friendsUsernames.stream().filter(Objects::nonNull).map(x -> userRepo.findByUsername(x).get()).toList();

        System.out.println(friends.toString());

        return friends.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void sendPushNotificationToUser(String recipient, String title, String message) throws PushClientException, InterruptedException {
        validateToken(recipient);
        List<ExpoPushTicket> allTickets = sendNotification(recipient, title, message);
        fetchReceipts(allTickets);
    }

    private void validateToken(String recipient) {
        if (!PushClient.isExponentPushToken(recipient))
            throw new CustomException("Token:" + recipient + " is not a valid token.\n\n");
    }

    private List<ExpoPushTicket> sendNotification(String recipient, String title, String message) throws PushClientException, InterruptedException {
        ExpoPushMessage expoPushMessage = new ExpoPushMessage();
        expoPushMessage.getTo().add(recipient);
        expoPushMessage.setTitle(title);
        expoPushMessage.setBody(message);

        List<ExpoPushMessage> expoPushMessages = new ArrayList<>();
        expoPushMessages.add(expoPushMessage);
        List<List<ExpoPushMessage>> chunks = pushClient.chunkPushNotifications(expoPushMessages);
        List<CompletableFuture<List<ExpoPushTicket>>> messageRepliesFutures = new ArrayList<>();

        for (List<ExpoPushMessage> chunk : chunks) {
            messageRepliesFutures.add(pushClient.sendPushNotificationsAsync(chunk));
        }

        List<ExpoPushTicket> allTickets = new ArrayList<>();
        for (CompletableFuture<List<ExpoPushTicket>> messageReplyFuture : messageRepliesFutures) {
            try {
                allTickets.addAll(messageReplyFuture.get());
            } catch (InterruptedException | ExecutionException e) {
                log.error("Error while sending notifications\n\n", e);
            }
        }
        return allTickets;
    }

    private void fetchReceipts(List<ExpoPushTicket> allTickets) {
        // Countdown 30s
        // Optimally, replace this with a better mechanism
        sleep();
        log.info("Fetching receipts...");

        List<String> ticketIds = allTickets.stream()
                .map(ExpoPushTicket::getId)
                .collect(Collectors.toList());
        CompletableFuture<List<ExpoPushReceipt>> receiptFutures = pushClient.getPushNotificationReceiptsAsync(ticketIds);

        List<ExpoPushReceipt> receipts;
        try {
            receipts = receiptFutures.get();
        } catch (ExecutionException | InterruptedException e) {
            log.error("Error while fetching receipts", e);
            return;
        }

        log.info("Received {} receipts", receipts.size());
        for (ExpoPushReceipt receipt : receipts) {
            log.info("Receipt for id: {} had status: {}", receipt.getId(), receipt.getStatus());
        }
    }

    private void sleep() {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

