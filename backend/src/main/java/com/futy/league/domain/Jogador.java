package com.futy.league.domain;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.TenantId;

@Entity
@Table(name = "jogador")
public class Jogador {

	@Id
	private UUID id;

	@TenantId
	@Column(name = "tenant_id", nullable = false)
	private UUID tenantId;

	@Column(nullable = false, length = 120)
	private String nome;

	@Column(length = 30)
	private String posicao;

	@Column(name = "criado_em", nullable = false)
	private Instant criadoEm;

	protected Jogador() {
	}

	public Jogador(UUID id, String nome, String posicao) {
		this.id = id;
		this.nome = nome;
		this.posicao = posicao;
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

	public String getPosicao() {
		return posicao;
	}

	public Instant getCriadoEm() {
		return criadoEm;
	}
}
