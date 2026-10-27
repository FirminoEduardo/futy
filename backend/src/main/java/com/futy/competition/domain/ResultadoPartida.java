package com.futy.competition.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "resultado_partida")
public class ResultadoPartida {

    @Id
    private UUID id;

    @Column(name = "partida_id", nullable = false)
    private UUID partidaId;

    @Column(name = "time_casa_id", nullable = false)
    private UUID timeCasaId;

    @Column(name = "time_visitante_id", nullable = false)
    private UUID timeVisitanteId;

    @Column(name = "gols_casa", nullable = false)
    private Integer golsCasa;

    @Column(name = "gols_visitante", nullable = false)
    private Integer golsVisitante;

    protected ResultadoPartida() {
    }

    public ResultadoPartida(UUID id, UUID partidaId, UUID timeCasaId, UUID timeVisitanteId, Integer golsCasa, Integer golsVisitante) {
        this.id = id;
        this.partidaId = partidaId;
        this.timeCasaId = timeCasaId;
        this.timeVisitanteId = timeVisitanteId;
        this.golsCasa = golsCasa;
        this.golsVisitante = golsVisitante;
    }

    public UUID getId() {
        return id;
    }

    public UUID getPartidaId() {
        return partidaId;
    }

    public UUID getTimeCasaId() {
        return timeCasaId;
    }

    public UUID getTimeVisitanteId() {
        return timeVisitanteId;
    }

    public Integer getGolsCasa() {
        return golsCasa;
    }

    public Integer getGolsVisitante() {
        return golsVisitante;
    }
}

