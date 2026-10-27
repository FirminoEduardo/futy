package com.futy.competition.web.dto;

import java.util.UUID;

import com.futy.competition.domain.Temporada;

public record TemporadaResponse(UUID id, String nome, String status) {

    public static TemporadaResponse from(Temporada temporada) {
        return new TemporadaResponse(temporada.getId(), temporada.getNome(), temporada.getStatus().name());
    }
}

