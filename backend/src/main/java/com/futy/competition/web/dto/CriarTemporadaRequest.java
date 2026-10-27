package com.futy.competition.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarTemporadaRequest(
        @NotBlank @Size(max = 120) String nome
) {
}

