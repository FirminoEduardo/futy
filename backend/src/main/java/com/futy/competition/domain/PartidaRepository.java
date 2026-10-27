package com.futy.competition.domain;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PartidaRepository extends JpaRepository<Partida, UUID> {

    List<Partida> findByTemporadaIdOrderByRodadaAsc(UUID temporadaId);
}

