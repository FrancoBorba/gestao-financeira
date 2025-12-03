package com.franco.gestao_financeira.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.franco.gestao_financeira.domain.model.Friendship;
import com.franco.gestao_financeira.domain.model.User;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    
    // Busca a entidade completa (para checar status)
    Optional<Friendship> findByRequesterAndReceiver(User requester, User receiver);
}