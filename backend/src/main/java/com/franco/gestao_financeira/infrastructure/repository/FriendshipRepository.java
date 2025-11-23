package com.franco.gestao_financeira.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.franco.gestao_financeira.domain.model.Friendship;
import com.franco.gestao_financeira.domain.model.User;


public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Optional<Friendship>  findByRequesterAndReceiver(User franco, User mariana);
    
}
