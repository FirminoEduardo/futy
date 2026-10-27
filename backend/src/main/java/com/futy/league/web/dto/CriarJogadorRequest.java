package com.futy.league.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarJogadorRequest(
		@NotBlank @Size(max = 120) String nome,
		@Size(max = 30) String posicao) {
}
