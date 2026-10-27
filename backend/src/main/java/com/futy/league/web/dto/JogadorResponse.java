package com.futy.league.web.dto;

import java.time.Instant;
import java.util.UUID;

import com.futy.league.domain.Jogador;

public record JogadorResponse(UUID id, UUID tenantId, String nome, String posicao, Instant criadoEm) {

	public static JogadorResponse from(Jogador jogador) {
		return new JogadorResponse(
				jogador.getId(),
				jogador.getTenantId(),
				jogador.getNome(),
				jogador.getPosicao(),
				jogador.getCriadoEm());
	}
}
