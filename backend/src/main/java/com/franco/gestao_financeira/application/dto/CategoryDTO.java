package com.franco.gestao_financeira.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CategoryDTO(
    @NotBlank(message = "O nome da categoria é obrigatório")
    String name,

    @NotBlank(message = "A cor é obrigatória")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Formato de cor inválido (Ex: #FF0000)")
    String colorHex
) {}