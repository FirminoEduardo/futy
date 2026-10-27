package com.futy.competition.web.dto;

import java.util.UUID;

import com.futy.competition.domain.Partida;

public record PartidaResponse(UUID id,
                             UUID temporadaId,
                             UUID timeCasaId,
                             UUID timeVisitanteId,
                             Integer rodada,
                             Integer golsCasa,
                             Integer golsVisitante,
                             String status) {

    public static PartidaResponse from(Partida partida) {
        return new PartidaResponse(
                partida.getId(),
                partida.getTemporadaId(),
                partida.getTimeCasaId(),
                partida.getTimeVisitanteId(),
                partida.getRodada(),
                partida.getGolsCasa(),
                partida.getGolsVisitante(),
                partida.getStatus().name()
        );
    }
}
