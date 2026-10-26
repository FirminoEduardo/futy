package com.futy.league.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarTimeRequest(@NotBlank @Size(max = 100) String nome) {
}
