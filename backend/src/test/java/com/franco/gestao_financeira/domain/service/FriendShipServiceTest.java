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

import com.franco.gestao_financeira.domain.exception.BusinessRuleException;
import com.franco.gestao_financeira.domain.model.Friendship;
import com.franco.gestao_financeira.domain.model.User;
import com.franco.gestao_financeira.domain.model.Enums.StatusFriendship;
import com.franco.gestao_financeira.infrastructure.repository.FriendshipRepository;
import com.franco.gestao_financeira.infrastructure.repository.UserRepository;



    

@ExtendWith(MockitoExtension.class)
public class FriendShipServiceTest {

    @Mock
    FriendshipRepository friendshipRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    FriendShipService friendShipService;


    User franco;
    User mariana;

    @BeforeEach
    public void setUp() {
        franco = new User(1L , "Franco" , "franco.borba@email.com" , 1);
        mariana = new User(2L , "Mariana" , "mariana@email,com",15);
    }

    @Test
    void shouldCreateFriendshipWithPendingStatusWhenRequestIsSent() {
     // Arrange

     when(userRepository.findById(1L)).thenReturn(Optional.of(franco));
     when(userRepository.findById(2L)).thenReturn(Optional.of(mariana));
     when(friendshipRepository.findByRequesterAndReceiver(franco, mariana)).thenReturn(Optional.empty());

     // Simulates saving by returning the object with PENDING status
     when(friendshipRepository.save(any(Friendship.class))).thenAnswer(
      invocation -> {
        Friendship f = invocation.getArgument(0);
        f.setId(1L);
        return f;
      }
     );

     // Act

     Friendship invite = friendShipService.sendFriendRequest(1L , 2L);
   
     // Assert

     assertNotNull(invite);
     assertEquals(StatusFriendship.PENDING, invite.getStatusFriendship());
     assertEquals(franco, invite.getRequester());
     assertEquals(mariana, invite.getReceiver());

    }

    @Test
    void shouldThrowExceptionWhenSenderIsSameAsReceiver() {

      // Arrange
     Long francoID = franco.getId();

     // Act ; Assert
     Exception exception = assertThrows(BusinessRuleException.class, 
      () -> { friendShipService.sendFriendRequest(francoID, francoID);

  });

     String message = "You may not friendship request to your self";

     assertEquals(message, exception.getMessage());
     
     verify(friendshipRepository , never()).save(any());
    }

  @Test
void shouldThrowExceptionWhenFriendshipIsAlreadyAccepted() {
    // ARRANGE
    when(userRepository.findById(franco.getId())).thenReturn(Optional.of(franco));
    when(userRepository.findById(mariana.getId())).thenReturn(Optional.of(mariana));

    // Create the object with status acceptd
    Friendship friendshipExists = new Friendship();
    friendshipExists.setRequester(franco);
    friendshipExists.setReceiver(mariana);
   friendshipExists.setStatusFriendship(StatusFriendship.ACCEPTED);

  
    when(friendshipRepository.findByRequesterAndReceiver(franco, mariana))
            .thenReturn(Optional.of(friendshipExists));

    // 2. ACT & ASSERT
    Exception exception = assertThrows(BusinessRuleException.class, () -> {
        friendShipService.sendFriendRequest(franco.getId(), mariana.getId());
    });


    assertEquals("You already friend", exception.getMessage());
}

@Test
void shouldThrowExceptionWhenRequestIsPending() {
    // 1. ARRANGE
    when(userRepository.findById(franco.getId())).thenReturn(Optional.of(franco));
    when(userRepository.findById(mariana.getId())).thenReturn(Optional.of(mariana));

    // Cria o objeto simulado com status PENDENTE
    Friendship pendindInvitation = new Friendship();
    pendindInvitation.setRequester(franco);
    pendindInvitation.setReceiver(mariana);
    pendindInvitation.setStatusFriendship(StatusFriendship.PENDING);


    when(friendshipRepository.findByRequesterAndReceiver(franco, mariana))
            .thenReturn(Optional.of(pendindInvitation));

    //  ACT & ASSERT
    Exception exception = assertThrows(BusinessRuleException.class, () -> {
        friendShipService.sendFriendRequest(franco.getId(), mariana.getId());
    });

    assertEquals("There is already a pending request", exception.getMessage());
}

    @Test
    void shouldUpdateStatusToAcceptedWhenRequestIsAccepted() {
     
        Long idInvitation = 99L;

        // Create the invitation
    Friendship pendindInvitation = new Friendship();
    pendindInvitation.setId(99L);
    pendindInvitation.setRequester(franco);
    pendindInvitation.setReceiver(mariana);
    pendindInvitation.setStatusFriendship(StatusFriendship.PENDING);

    when(friendshipRepository.findById(idInvitation))
                .thenReturn(Optional.of(pendindInvitation));

     
    when(friendshipRepository.save(any(Friendship.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Friendship result = friendShipService.acceptFriendRequest(idInvitation);

        //  ASSERT 
        assertNotNull(result);
        
        assertEquals(StatusFriendship.ACCEPTED , result.getStatusFriendship());
        
        verify(friendshipRepository).save(pendindInvitation);
    }
}
