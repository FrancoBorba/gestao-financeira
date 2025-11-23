package com.franco.gestao_financeira.domain.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.franco.gestao_financeira.domain.exception.BusinessRuleException;
import com.franco.gestao_financeira.domain.model.Friendship;
import com.franco.gestao_financeira.domain.model.User;
import com.franco.gestao_financeira.domain.model.Enums.StatusFriendship;
import com.franco.gestao_financeira.infrastructure.repository.FriendshipRepository;
import com.franco.gestao_financeira.infrastructure.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class FriendShipService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendshipRepository friendshipRepository;



    @Transactional
    public Friendship sendFriendRequest(Long requesterId, String nickname) {

        User reciverUser = userRepository.findByNickname(nickname).orElseThrow(
            () -> new BusinessRuleException("User with nickname " + nickname + "not found")
        );
        
        if (requesterId.equals(reciverUser.getId())) {
            throw new BusinessRuleException("You may not friendship request to your self");
        }

        User requesterUser = userRepository.findById(requesterId).orElseThrow(
           () -> new RuntimeException("User with id " + requesterId + "not found")
        );


        Optional<Friendship> existing = friendshipRepository.findByRequesterAndReceiver(requesterUser, reciverUser);

        if (existing.isPresent()) {
            Friendship friendship = existing.get();
            if (friendship.getStatusFriendship() == StatusFriendship.ACCEPTED) {
                throw new BusinessRuleException("You already friend");
            } else if (friendship.getStatusFriendship() == StatusFriendship.PENDING) {
                throw new BusinessRuleException("There is already a pending request");
            }
        }

        Friendship newFriendship = new Friendship();
        newFriendship.setRequester(requesterUser);
        newFriendship.setReceiver(reciverUser);
        newFriendship.setStatusFriendship(StatusFriendship.PENDING);

        return friendshipRepository.save(newFriendship);


    }

   @Transactional
    public Friendship acceptFriendRequest(Long friendshipId) {
        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));
 
        friendship.setStatusFriendship(StatusFriendship.ACCEPTED);
        return friendshipRepository.save(friendship);
    }

}
