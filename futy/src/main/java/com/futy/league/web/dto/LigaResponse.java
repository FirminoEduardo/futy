package com.futy.league.web.dto;

import java.time.Instant;
import java.util.UUID;

import com.futy.league.domain.Liga;

public record LigaResponse(UUID id, String slug, String nome, Instant criadaEm) {

	public static LigaResponse from(Liga liga) {
		return new LigaResponse(liga.getId(), liga.getSlug(), liga.getNome(), liga.getCriadaEm());
	}
}
