package com.futy.competition.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.TenantId;

@Entity
@Table(name = "classificacao")
public class Classificacao {

    @Id
    private UUID id;

    @TenantId
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "temporada_id", nullable = false)
    private UUID temporadaId;

    @Column(name = "time_id", nullable = false)
    private UUID timeId;

    @Column(nullable = false)
    private Integer pontos;

    @Column(nullable = false)
    private Integer jogos;

    @Column(nullable = false)
    private Integer vitorias;

    @Column(nullable = false)
    private Integer empates;

    @Column(nullable = false)
    private Integer derrotas;

    @Column(nullable = false)
    private Integer gols_feitos;

    @Column(nullable = false)
    private Integer gols_sofridos;

    @Column(nullable = false)
    private Integer saldo_gols;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusClassificacao status;

    protected Classificacao() {
    }

    public Classificacao(UUID id, UUID temporadaId, UUID timeId) {
        this.id = id;
        this.temporadaId = temporadaId;
        this.timeId = timeId;
        this.pontos = 0;
        this.jogos = 0;
        this.vitorias = 0;
        this.empates = 0;
        this.derrotas = 0;
        this.gols_feitos = 0;
        this.gols_sofridos = 0;
        this.saldo_gols = 0;
        this.status = StatusClassificacao.NORMAL;
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

    public UUID getTimeId() {
        return timeId;
    }

    public Integer getPontos() {
        return pontos;
    }

    public Integer getJogos() {
        return jogos;
    }

    public Integer getVitorias() {
        return vitorias;
    }

    public Integer getEmpates() {
        return empates;
    }

    public Integer getDerrotas() {
        return derrotas;
    }

    public Integer getGolsFeitos() {
        return gols_feitos;
    }

    public Integer getGolsSofridos() {
        return gols_sofridos;
    }

    public Integer getSaldoGols() {
        return saldo_gols;
    }

    public StatusClassificacao getStatus() {
        return status;
    }

    public void aplicarResultado(Integer golsFeitos, Integer golsSofridos) {
        this.jogos++;
        this.gols_feitos += golsFeitos;
        this.gols_sofridos += golsSofridos;
        this.saldo_gols = this.gols_feitos - this.gols_sofridos;

        if (golsFeitos > golsSofridos) {
            this.vitorias++;
            this.pontos += 3;
        } else if (golsFeitos.equals(golsSofridos)) {
            this.empates++;
            this.pontos += 1;
        } else {
            this.derrotas++;
        }
    }
}

