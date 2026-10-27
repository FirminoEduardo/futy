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
@Table(name = "temporada")
public class Temporada {

    @Id
    private UUID id;

    @TenantId
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false, length = 120)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private StatusTemporada status;

    @Column(name = "criada_em", nullable = false)
    private Instant criadaEm;

    protected Temporada() {
    }

    public Temporada(UUID id, String nome, StatusTemporada status) {
        this.id = id;
        this.nome = nome;
        this.status = status;
        this.criadaEm = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public String getNome() {
        return nome;
    }

    public StatusTemporada getStatus() {
        return status;
    }

    public Instant getCriadaEm() {
        return criadaEm;
    }
}

