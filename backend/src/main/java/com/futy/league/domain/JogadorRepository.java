package com.futy.league.domain;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JogadorRepository extends JpaRepository<Jogador, UUID> {
}
