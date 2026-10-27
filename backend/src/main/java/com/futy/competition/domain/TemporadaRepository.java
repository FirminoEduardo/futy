package com.futy.competition.domain;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporadaRepository extends JpaRepository<Temporada, UUID> {

    List<Temporada> findAllByOrderByCriadaEmDesc();
}

