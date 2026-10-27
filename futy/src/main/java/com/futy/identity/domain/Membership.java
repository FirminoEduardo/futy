package com.futy.identity.domain;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "liga_membro", uniqueConstraints = @UniqueConstraint(columnNames = { "liga_id", "usuario_id" }))
public class Membership {

    @Id
    private UUID id;

    @Column(name = "liga_id", nullable = false)
    private UUID ligaId;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Papel papel;

    @Column(name = "criado_em", nullable = false)
    private Instant criadoEm;

    protected Membership() {
    }

    public Membership(UUID id, UUID ligaId, UUID usuarioId, Papel papel) {
        this.id = id;
        this.ligaId = ligaId;
        this.usuarioId = usuarioId;
        this.papel = papel;
        this.criadoEm = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getLigaId() {
        return ligaId;
    }

    public UUID getUsuarioId() {
        return usuarioId;
    }

    public Papel getPapel() {
        return papel;
    }

    public Instant getCriadoEm() {
        return criadoEm;
    }
}

