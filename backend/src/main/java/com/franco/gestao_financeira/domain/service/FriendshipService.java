package com.franco.gestao_financeira.domain.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.franco.gestao_financeira.application.dto.FriendRequestDTO;
import com.franco.gestao_financeira.application.dto.FriendshipResponseDTO;
import com.franco.gestao_financeira.application.mapper.FriendshipMapper;
import com.franco.gestao_financeira.domain.exception.BusinessRuleException;
import com.franco.gestao_financeira.domain.exception.ResourceNotFoundException;
import com.franco.gestao_financeira.domain.model.Friendship;
import com.franco.gestao_financeira.domain.model.User;
import com.franco.gestao_financeira.domain.model.Enums.FriendshipStatus;
import com.franco.gestao_financeira.infrastructure.repository.FriendshipRepository;
import com.franco.gestao_financeira.infrastructure.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class FriendshipService {

    @Autowired
    FriendshipRepository friendshipRepository;


    @Autowired 
    UserRepository userRepository;

    @Autowired
    FriendshipMapper friendshipMapper;

    private static final Logger logger = LoggerFactory.getLogger(FriendshipService.class);

    @Transactional
    public FriendshipResponseDTO sendFriendRequest(FriendRequestDTO dto) {
        logger.info("Iniciando processo de solicitação de amizade: Requester ID {} -> Receiver Nick {}", 
                dto.requesterId(), dto.receiverNickname());

        User requester = findUserById(dto.requesterId());
        User receiver = findUserByNickname(dto.receiverNickname());


        validateSelfRequest(requester, receiver);
        validateExistingRelationship(requester, receiver);

        Friendship newFriendship = new Friendship();
        newFriendship.setRequester(requester);
        newFriendship.setReceiver(receiver);
        newFriendship.setStatusFriendship(FriendshipStatus.PENDING);

        Friendship saved = friendshipRepository.save(newFriendship);
        
        logger.info("Solicitação de amizade criada com sucesso. ID: {}", saved.getId());


        return friendshipMapper.toDTO(saved);
    }

    @Transactional
    public FriendshipResponseDTO acceptFriendRequest(Long friendshipId) {
        logger.info("Processando aceite de amizade para o ID: {}", friendshipId);

        Friendship friendship = friendshipRepository.findById(friendshipId)
                .orElseThrow(() -> {
                    logger.error("Tentativa de aceitar solicitação inexistente: {}", friendshipId);
                    return new ResourceNotFoundException("Solicitação não encontrada");
                });

        if (friendship.getStatusFriendship() == FriendshipStatus.ACCEPTED) {
            logger.warn("Tentativa de aceitar amizade já aceita: {}", friendshipId);
            throw new BusinessRuleException("Esta amizade já foi aceita anteriormente");
        }

        friendship.setStatusFriendship(FriendshipStatus.ACCEPTED);
        Friendship saved = friendshipRepository.save(friendship);

        logger.info("Amizade ID {} confirmada com sucesso.", saved.getId());

        return friendshipMapper.toDTO(saved);
    }

    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário solicitante não encontrado com ID: " + id));
    }

    private User findUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário destinatário não encontrado com Nickname: " + nickname));
    }

    private void validateSelfRequest(User requester, User receiver) {
        if (requester.getId().equals(receiver.getId())) {
            logger.warn("Usuário {} tentou adicionar a si mesmo.", requester.getId());
            throw new BusinessRuleException("Você não pode enviar pedido de amizade para si mesmo");
        }
    }

    private void validateExistingRelationship(User requester, User receiver) {
        Optional<Friendship> existing = friendshipRepository.findByRequesterAndReceiver(requester, receiver);
        
        if (existing.isPresent()) {
            Friendship f = existing.get();
            if (f.getStatusFriendship() == FriendshipStatus.ACCEPTED) {
                throw new BusinessRuleException("You already friend");
            } else if (f.getStatusFriendship() == FriendshipStatus.PENDING) {
                throw new BusinessRuleException("Já existe uma solicitação pendente");
            }
        }
    }
    
}
