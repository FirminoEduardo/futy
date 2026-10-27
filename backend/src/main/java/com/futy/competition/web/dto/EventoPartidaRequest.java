package com.futy.competition.web.dto;

import java.util.UUID;

import com.futy.competition.domain.TipoEventoPartida;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EventoPartidaRequest(
        @NotNull UUID timeId,
        UUID jogadorId,
        @NotNull @Min(1) Integer minuto,
        @NotNull TipoEventoPartida tipo,
        String descricao
) {
}

