package com.futy.competition.web.dto;

import java.util.UUID;

import com.futy.competition.domain.EventoPartida;

public record EventoPartidaResponse(UUID id, UUID partidaId, UUID timeId, UUID jogadorId, Integer minuto, String tipo, String descricao) {

    public static EventoPartidaResponse from(EventoPartida evento) {
        return new EventoPartidaResponse(
                evento.getId(),
                evento.getPartidaId(),
                evento.getTimeId(),
                evento.getJogadorId(),
                evento.getMinuto(),
                evento.getTipo().name(),
                evento.getDescricao()
        );
    }
}

