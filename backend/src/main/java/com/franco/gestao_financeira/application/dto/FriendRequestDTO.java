package com.franco.gestao_financeira.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FriendRequestDTO(
    @NotNull 
    Long requesterId,

    @NotBlank 
    String receiverNickname
) {}