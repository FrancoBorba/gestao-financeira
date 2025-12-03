package com.franco.gestao_financeira.application.mapper;

import com.franco.gestao_financeira.application.dto.FriendshipResponseDTO;
import com.franco.gestao_financeira.domain.model.Friendship;
import org.springframework.stereotype.Component;

@Component
public class FriendshipMapper {

    public FriendshipResponseDTO toDTO(Friendship entity) {
        if (entity == null) {
            return null;
        }
        
        return new FriendshipResponseDTO(
            entity.getId(),
            entity.getRequester().getName(),
            entity.getReceiver().getName(),
            entity.getStatusFriendship().toString(),
            entity.getCreatedAt()
        );
    }
}