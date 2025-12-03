package com.franco.gestao_financeira.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.franco.gestao_financeira.application.dto.FriendRequestDTO;
import com.franco.gestao_financeira.application.dto.FriendshipResponseDTO;
import com.franco.gestao_financeira.domain.exception.BusinessRuleException;
import com.franco.gestao_financeira.domain.model.Friendship;
import com.franco.gestao_financeira.domain.model.User;
import com.franco.gestao_financeira.domain.model.Enums.FriendshipStatus;
import com.franco.gestao_financeira.infrastructure.repository.FriendshipRepository;
import com.franco.gestao_financeira.infrastructure.repository.UserRepository;


@ExtendWith(MockitoExtension.class)
public class FriendshipServiceTest {

    @Mock
    FriendshipRepository friendshipRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    FriendshipService friendshipService;

    User franco;
    User mariana;

    @BeforeEach
    public void setUp() {
        franco = new User(1L, "Franco Borba", "franco@email.com", "franco.dev", 1);
        mariana = new User(2L, "Mariana Azevedo", "mariana@email.com", "mariana.fin", 15);
    }


    @Test
    void shouldCreateFriendshipWithPendingStatusWhenRequestIsSent() {
        // Arrange
        FriendRequestDTO requestDto = new FriendRequestDTO(1L, "mariana.fin");

        when(userRepository.findById(1L)).thenReturn(Optional.of(franco));
        when(userRepository.findByNickname("mariana.fin")).thenReturn(Optional.of(mariana));
        

        when(friendshipRepository.findByRequesterAndReceiver(franco, mariana)).thenReturn(Optional.empty());


        when(friendshipRepository.save(any(Friendship.class))).thenAnswer(invocation -> {
            Friendship f = invocation.getArgument(0);
            f.setId(100L); // Banco gerando ID
            return f;
        });

        // Act
        FriendshipResponseDTO response = friendshipService.sendFriendRequest(requestDto);

        // Assert
        assertNotNull(response);
        assertEquals(100L, response.id());
        assertEquals("PENDING", response.status());
        assertEquals("Franco Borba", response.requesterName());
        assertEquals("Mariana Azevedo", response.receiverName());
    }

    @Test
    void shouldThrowExceptionWhenSenderIsSameAsReceiver() {
        // Arrange
    
        FriendRequestDTO requestDto = new FriendRequestDTO(franco.getId(), franco.getNickname());

        when(userRepository.findById(franco.getId())).thenReturn(Optional.of(franco));

        when(userRepository.findByNickname(franco.getNickname())).thenReturn(Optional.of(franco));

        // Act & Assert
        Exception exception = assertThrows(BusinessRuleException.class, () -> {
            friendshipService.sendFriendRequest(requestDto);
        });

        assertEquals("You may not friendship request to your self", exception.getMessage());
        verify(friendshipRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenFriendshipIsAlreadyAccepted() {
        // Arrange
        FriendRequestDTO requestDto = new FriendRequestDTO(1L, "mariana.fin");

        when(userRepository.findById(1L)).thenReturn(Optional.of(franco));
        when(userRepository.findByNickname("mariana.fin")).thenReturn(Optional.of(mariana));

        Friendship existing = new Friendship();
        existing.setRequester(franco);
        existing.setReceiver(mariana);
        existing.setStatusFriendship(FriendshipStatus.ACCEPTED);

        when(friendshipRepository.findByRequesterAndReceiver(franco, mariana))
                .thenReturn(Optional.of(existing));

        // Act & Assert
        Exception exception = assertThrows(BusinessRuleException.class, () -> {
            friendshipService.sendFriendRequest(requestDto);
        });

        assertEquals("You already friend", exception.getMessage());
        verify(friendshipRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenRequestIsPending() {
        // Arrange
        FriendRequestDTO requestDto = new FriendRequestDTO(1L, "mariana.fin");

        when(userRepository.findById(1L)).thenReturn(Optional.of(franco));
        when(userRepository.findByNickname("mariana.fin")).thenReturn(Optional.of(mariana));

        Friendship pending = new Friendship();
        pending.setRequester(franco);
        pending.setReceiver(mariana);
        pending.setStatusFriendship(FriendshipStatus.PENDING);

        when(friendshipRepository.findByRequesterAndReceiver(franco, mariana))
                .thenReturn(Optional.of(pending));

        // Act & Assert
        Exception exception = assertThrows(BusinessRuleException.class, () -> {
            friendshipService.sendFriendRequest(requestDto);
        });

        assertEquals("There is already a pending request", exception.getMessage());
        verify(friendshipRepository, never()).save(any());
    }

    @Test
    void shouldUpdateStatusToAcceptedWhenRequestIsAccepted() {
        // Arrange
        Long idInvitation = 99L;


        Friendship pendingInvitation = new Friendship();
        pendingInvitation.setId(idInvitation);
        pendingInvitation.setRequester(franco);
        pendingInvitation.setReceiver(mariana);
        pendingInvitation.setStatusFriendship(FriendshipStatus.PENDING);

        when(friendshipRepository.findById(idInvitation)).thenReturn(Optional.of(pendingInvitation));
        
        when(friendshipRepository.save(any(Friendship.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        FriendshipResponseDTO response = friendshipService.acceptFriendRequest(idInvitation);

        // Assert
        assertNotNull(response);
        assertEquals("ACCEPTED", response.status()); 
        

        assertEquals(FriendshipStatus.ACCEPTED, pendingInvitation.getStatusFriendship());
    }
}