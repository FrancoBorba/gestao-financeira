package com.franco.gestao_financeira.application.dto;

import java.time.LocalDateTime;

public record FriendshipResponseDTO(
    Long id,
    String requesterName,
    String receiverName,
    String status,
    LocalDateTime createdAt
) {}