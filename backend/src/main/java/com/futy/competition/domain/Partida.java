package com.futy.competition.domain;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.TenantId;

@Entity
@Table(name = "partida")
public class Partida {

    @Id
    private UUID id;

    @TenantId
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "temporada_id", nullable = false)
    private UUID temporadaId;

    @Column(name = "time_casa_id", nullable = false)
    private UUID timeCasaId;

    @Column(name = "time_visitante_id", nullable = false)
    private UUID timeVisitanteId;

    @Column(nullable = false)
    private Integer rodada;

    @Column(name = "gols_casa", nullable = false)
    private Integer golsCasa;

    @Column(name = "gols_visitante", nullable = false)
    private Integer golsVisitante;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusPartida status;

    @Column(name = "criada_em", nullable = false)
    private Instant criadaEm;

    protected Partida() {
    }

    public Partida(UUID id, UUID temporadaId, UUID timeCasaId, UUID timeVisitanteId, Integer rodada) {
        this.id = id;
        this.temporadaId = temporadaId;
        this.timeCasaId = timeCasaId;
        this.timeVisitanteId = timeVisitanteId;
        this.rodada = rodada;
        this.golsCasa = 0;
        this.golsVisitante = 0;
        this.status = StatusPartida.AGENDADA;
        this.criadaEm = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public UUID getTemporadaId() {
        return temporadaId;
    }

    public UUID getTimeCasaId() {
        return timeCasaId;
    }

    public UUID getTimeVisitanteId() {
        return timeVisitanteId;
    }

    public Integer getRodada() {
        return rodada;
    }

    public Integer getGolsCasa() {
        return golsCasa;
    }

    public Integer getGolsVisitante() {
        return golsVisitante;
    }

    public StatusPartida getStatus() {
        return status;
    }

    public Instant getCriadaEm() {
        return criadaEm;
    }

    public void registrarResultado(Integer golsCasa, Integer golsVisitante) {
        this.golsCasa = golsCasa;
        this.golsVisitante = golsVisitante;
        this.status = StatusPartida.CONCLUIDA;
    }
}
