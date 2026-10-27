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
@Table(name = "evento_partida")
public class EventoPartida {

    @Id
    private UUID id;

    @TenantId
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "partida_id", nullable = false)
    private UUID partidaId;

    @Column(name = "time_id", nullable = false)
    private UUID timeId;

    @Column(name = "jogador_id")
    private UUID jogadorId;

    @Column(nullable = false)
    private Integer minuto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoEventoPartida tipo;

    @Column(length = 255)
    private String descricao;

    @Column(name = "criado_em", nullable = false)
    private Instant criadoEm;

    protected EventoPartida() {
    }

    public EventoPartida(UUID id, UUID partidaId, UUID timeId, UUID jogadorId, Integer minuto, TipoEventoPartida tipo, String descricao) {
        this.id = id;
        this.partidaId = partidaId;
        this.timeId = timeId;
        this.jogadorId = jogadorId;
        this.minuto = minuto;
        this.tipo = tipo;
        this.descricao = descricao;
        this.criadoEm = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public UUID getPartidaId() {
        return partidaId;
    }

    public UUID getTimeId() {
        return timeId;
    }

    public UUID getJogadorId() {
        return jogadorId;
    }

    public Integer getMinuto() {
        return minuto;
    }

    public TipoEventoPartida getTipo() {
        return tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }
}

