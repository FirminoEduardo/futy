package com.futy.league.domain;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.hibernate.annotations.TenantId;

@Entity
@Table(name = "time", uniqueConstraints = @UniqueConstraint(columnNames = { "tenant_id", "nome" }))
public class Time {

	@Id
	private UUID id;

	@TenantId
	@Column(name = "tenant_id", nullable = false)
	private UUID tenantId;

	@Column(nullable = false, length = 100)
	private String nome;

	@Column(name = "criado_em", nullable = false)
	private Instant criadoEm;

	protected Time() {
	}

	public Time(UUID id, String nome) {
		this.id = id;
		this.nome = nome;
		this.criadoEm = Instant.now();
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

	public Instant getCriadoEm() {
		return criadoEm;
	}
}
