package com.futy.league.domain;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "liga")
public class Liga {

	@Id
	private UUID id;

	@Column(nullable = false, unique = true, length = 60)
	private String slug;

	@Column(nullable = false, length = 120)
	private String nome;

	@Column(name = "criada_em", nullable = false)
	private Instant criadaEm;

	protected Liga() {
	}

	public Liga(UUID id, String slug, String nome) {
		this.id = id;
		this.slug = slug;
		this.nome = nome;
		this.criadaEm = Instant.now();
	}

	public UUID getId() {
		return id;
	}

	public String getSlug() {
		return slug;
	}

	public String getNome() {
		return nome;
	}

	public Instant getCriadaEm() {
		return criadaEm;
	}
}
