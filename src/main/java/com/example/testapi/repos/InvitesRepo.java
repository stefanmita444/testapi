package com.example.testapi.repos;

import com.example.testapi.models.Invite;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitesRepo extends MongoRepository<Invite, String>{
    Optional<Invite> findByRequesterIdAndReceiverId(String requesterId, String receiverId);
    List<Invite> findByRequesterIdOrReceiverId(String requesterId, String receiverId);


}
