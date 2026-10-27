package com.futy.league.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CriarLigaRequest(
		@NotBlank @Size(max = 60)
		@Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "slug deve ser lowercase com hifens")
		String slug,

		@NotBlank @Size(max = 120) String nome) {
}
